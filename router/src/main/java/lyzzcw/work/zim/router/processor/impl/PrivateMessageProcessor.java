package lyzzcw.work.zim.router.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.file.FileNameUtil;
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
import lyzzcw.work.component.common.file.FileUtil;
import lyzzcw.work.component.common.id.SnowflakeIdWorker;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.component.common.utils.EncryptUtil;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.component.redis.cache.redis.list.RedisListService;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
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

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 私聊消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class PrivateMessageProcessor implements MessageProcessor<PrivateMessage> {

    static BeanCopier copier = BeanCopier.create(PrivateMessage.class, PrivateMessage.class, false);
    @Resource
    private RedisDistributeCacheService distributeCacheService;
    @Autowired
    @Qualifier("persistenceProducer")
    private MessageQueueProducer persistenceProducer;
    @Resource
    private RedisListService redisListService;
    @Resource
    private MinioTemplate minioTemplate;

    @Override
    public void process(PrivateMessage data) {
        log.info("received private message:{}",data);
        //生成唯一消息码
        this.handleMessage(data);
        //放入持久化队列
        persistenceProducer.sendMessage(RocketMQUtil.buildPersistenceMessage(
                JacksonUtil.to(this.persistenceData(data))));
        //回推给发送者消息
        if(data.getSendResult()){
            PrivateMessage result = new PrivateMessage();
            this.copy(data,result);
            result.setReceiveId(result.getSenderId());
            String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                    IMConstants.IM_USER_SERVER_ID, result.getReceiveId().toString());
            String serverId = distributeCacheService.get(redisKey);
            if(!StringUtils.isEmpty(serverId) && ProducerManager.getInstance().contains(serverId)){
                MessageQueueProducer producer = ProducerManager.getInstance()
                        .getServerProducer(serverId);
                //暂不需要保证有序，离线不做处理
                producer.sendMessage(getMessageInfo(result,serverId));
            }
        }
        //发送给接收人
        forwardToReceiver(data);
    }

    private void forwardToReceiver(PrivateMessage data) {
        //找到接收者所在的server id
        //记录用户的channelId
        String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                IMConstants.IM_USER_SERVER_ID, data.getReceiveId().toString());
        String serverId = distributeCacheService.get(redisKey);
        if(!StringUtils.isEmpty(serverId) && ProducerManager.getInstance().contains(serverId)){
            MessageQueueProducer producer = ProducerManager.getInstance()
                    .getServerProducer(serverId);
            producer.sendOrderMessage(getMessageInfo(data,serverId), data.getReceiveId().toString());
        }else{
            //消息逻辑
            log.info("private message receiver offLine:{}", data.getReceiveId());
            String offLineRedisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                    IMConstants.IM_USER_OFFLINE_MESSAGE, data.getReceiveId().toString());
            redisListService.leftPush(offLineRedisKey,JacksonUtil.to(getMutualInfo(data)));
        }
    }

    private void handleMessage(PrivateMessage data) {
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
                        "/private/"+messageCode+"."+imageFormat.getName(),uploadStream);
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
                        "/private/"+messageCode+"."+type,uploadStream);
                data.setData(url);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ImMessage persistenceData(PrivateMessage privateMessage) {
        ImMessage imMessage = new ImMessage();
        imMessage.setContent(privateMessage.getData().toString());
        imMessage.setMessageType(privateMessage.getMessageType());
        imMessage.setRecvId(privateMessage.getReceiveId());
        imMessage.setSendId(privateMessage.getSenderId());
        imMessage.setStatus(MessageStatus.SENDED.code());
        imMessage.setSendTime(LocalDateTime.now());
        imMessage.setCmdType(IMCmdType.PRIVATE_MESSAGE.code());
        imMessage.setMessageCode(privateMessage.getMessageCode());
        return imMessage;
    }

    private MessageInfo getMessageInfo(PrivateMessage data,String serverId) {
        MutualInfo<PrivateMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(serverId))
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    private MutualInfo<PrivateMessage> getMutualInfo(PrivateMessage data) {
        MutualInfo<PrivateMessage> mutualInfo = new MutualInfo.Builder<PrivateMessage>()
                .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                .info(data)
                .build();
        return mutualInfo;
    }

    @Override
    public PrivateMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new PrivateMessage(), false);
    }

    @Override
    public void copy(PrivateMessage source, PrivateMessage target) {
        copier.copy(source, target, null);
    }
}
