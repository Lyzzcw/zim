package lyzzcw.work.zim.router.processor.impl;


import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.GroupMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.enums.MessageStatus;
import lyzzcw.work.common.enums.MessageType;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.ProducerManager;
import lyzzcw.work.component.common.file.FileTypeUtils;
import lyzzcw.work.component.common.id.SnowflakeIdWorker;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.component.common.utils.EncryptUtil;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.component.redis.cache.redis.list.RedisListService;
import lyzzcw.work.zim.router.infrastructure.entity.ImGroupMember;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.infrastructure.mapper.ImGroupMemberMapper;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.rocketmq.RocketMQUtil;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 群消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class GroupMessageProcessor implements MessageProcessor<GroupMessage> {

    static BeanCopier copier = BeanCopier.create(GroupMessage.class, GroupMessage.class, false);

    @Resource
    private RedisDistributeCacheService distributeCacheService;
    @Autowired
    @Qualifier("persistenceProducer")
    private MessageQueueProducer persistenceProducer;
    @Resource
    private RedisListService redisListService;
    @Resource
    private ImGroupMemberMapper imGroupMemberMapper;
    @Resource
    private MinioTemplate minioTemplate;

    @Override
    public void process(GroupMessage data) {
        log.info("received group message:{}",data);
        this.handleMessage(data);
        //放入持久化队列
        persistenceProducer.sendMessage(RocketMQUtil.buildPersistenceMessage(
                JacksonUtil.to(this.persistenceData(data))));
        //找到群组接收者们所在的server id
        List<ImGroupMember> members = imGroupMemberMapper.findGroupMembersByGroupId(data.getGroupId());
        Assert.isTrue(!CollectionUtils.isEmpty(members),
                "group : " + data.getGroupId() + " has no members");
        members.forEach(member -> {
            if(member.getUserId().equals(data.getSenderId())){
                if(data.getSendResult()){
                    GroupMessage result = new GroupMessage();
                    this.copy(data,result);
                    result.setReceiveId(result.getSenderId());
                    String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                            IMConstants.IM_USER_SERVER_ID, result.getReceiveId().toString());
                    String serverId = distributeCacheService.get(redisKey);
                    if(!StringUtils.isEmpty(serverId) && ProducerManager.getInstance().contains(serverId)){
                        MessageQueueProducer producer = ProducerManager.getInstance()
                                .getServerProducer(serverId);
                        producer.sendMessage(getMessageInfo(result,serverId));
                    }
                }
                //发送者且不需要转发，server直接返回发送结果
                return;
            }
            data.setReceiveId(member.getUserId());
            //转发其他群成员
            forwardToReceiver(data);
        });

    }

    private void forwardToReceiver(GroupMessage data) {
        //记录用户的channelId
        String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                IMConstants.IM_USER_SERVER_ID, data.getReceiveId().toString());
        String serverId = distributeCacheService.get(redisKey);
        if(!StringUtils.isEmpty(serverId) && ProducerManager.getInstance().contains(serverId)){
            MessageQueueProducer producer = ProducerManager.getInstance()
                    .getServerProducer(serverId);
            producer.sendOrderMessage(getMessageInfo(data,serverId), data.getReceiveId().toString());
        }else{
            //离线消息逻辑
            log.info("group message receiver offLine:{}", data.getReceiveId());
            String offLineRedisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                    IMConstants.IM_USER_OFFLINE_MESSAGE, data.getReceiveId().toString());
            redisListService.leftPush(offLineRedisKey,JacksonUtil.to(getMutualInfo(data)));
        }
    }

    /**
     * 处理消息
     *
     * @param data 数据
     */
    private void handleMessage(GroupMessage data) {
        //生成唯一消息码
        Long messageCode = SnowflakeIdWorker.generateId();
        data.setMessageCode(messageCode);
        //处理图片格式消息
        if(data.getMessageType() == MessageType.IMAGE.code()){
            byte[] decodedBytes = EncryptUtil.base64_decode((String)data.getData());
            try(InputStream inputStream = new ByteArrayInputStream(decodedBytes);
                InputStream uploadStream = new ByteArrayInputStream(decodedBytes)){
                ImageInfo imageInfo = Imaging.getImageInfo(inputStream, null);
                // 获取图片格式信息
                ImageFormat imageFormat = imageInfo.getFormat();
                String url = minioTemplate.upload("zim",
                        "/group/"+messageCode+"."+imageFormat.getName(),uploadStream);
                data.setData(url);
            }catch (IOException | ImageReadException e) {
                e.printStackTrace();
            }
        }
        //处理文件格式消息
        if(data.getMessageType() == MessageType.FILE.code()){
            byte[] decodedBytes = EncryptUtil.base64_decode((String)data.getData());
            String type = FileTypeUtils.getFileTypeByBytes(decodedBytes);
            try(InputStream uploadStream = new ByteArrayInputStream(decodedBytes)){
                String url = minioTemplate.upload("zim",
                        "/group/"+messageCode+"."+type,uploadStream);
                data.setData(url);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ImMessage persistenceData(GroupMessage groupMessage) {
        ImMessage imMessage = new ImMessage();
        imMessage.setContent(groupMessage.getData().toString());
        imMessage.setMessageType(groupMessage.getMessageType());
        imMessage.setRecvId(groupMessage.getGroupId());
        imMessage.setSendId(groupMessage.getSenderId());
        imMessage.setStatus(MessageStatus.SENDED.code());
        imMessage.setSendTime(LocalDateTime.now());
        imMessage.setCmdType(IMCmdType.GROUP_MESSAGE.code());
        imMessage.setMessageCode(groupMessage.getMessageCode());
        return imMessage;
    }


    @Override
    public GroupMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new GroupMessage(), false);
    }

    @Override
    public void copy(GroupMessage source, GroupMessage target) {
        copier.copy(source, target, null);
    }

    private MessageInfo getMessageInfo(GroupMessage data, String serverId) {
        MutualInfo<GroupMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(serverId))
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    private MutualInfo<GroupMessage> getMutualInfo(GroupMessage data) {
        MutualInfo<GroupMessage> mutualInfo = new MutualInfo.Builder<GroupMessage>()
                .cmd(IMCmdType.GROUP_MESSAGE.code())
                .info(data)
                .build();
        return mutualInfo;
    }


}
