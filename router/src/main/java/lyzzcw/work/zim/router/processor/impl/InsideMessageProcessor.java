package lyzzcw.work.zim.router.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.InsideMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.domain.SystemMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.enums.MessageStatus;
import lyzzcw.work.common.enums.MessageType;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.ProducerManager;
import lyzzcw.work.component.common.id.SnowflakeIdWorker;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.component.redis.cache.redis.list.RedisListService;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.rocketmq.RocketMQUtil;
import lyzzcw.work.zim.router.service.ImMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 站内信处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class InsideMessageProcessor implements MessageProcessor<InsideMessage> {

    @Resource
    private RedisDistributeCacheService distributeCacheService;
    @Autowired
    @Qualifier("persistenceProducer")
    private MessageQueueProducer persistenceProducer;
    @Resource
    private RedisListService redisListService;
    @Resource
    private ImMessageService imMessageService;

    @Override
    public void process(InsideMessage data) {
        log.info("received inside message:{}",data);
        if(data.getMessageType().equals(MessageType.READED.code())){
            ImMessage updateMessage = new ImMessage();
            updateMessage.setMessageCode(Long.parseLong(data.getMessageCode()));
            updateMessage.setStatus(MessageStatus.READED.code());
            imMessageService.updateMessageByCode(updateMessage);
            return;
        }
        //生成唯一消息码
        Long messageCode = SnowflakeIdWorker.generateId();
        data.setMessageCode(messageCode+"");
        //放入持久化队列
        persistenceProducer.sendMessage(RocketMQUtil.buildPersistenceMessage(
                JacksonUtil.to(this.persistenceData(data))));
        //找到接收者所在的server id
        //记录用户的channelId
        String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                IMConstants.IM_USER_SERVER_ID, data.getReceiveId().toString());
        String serverId = distributeCacheService.get(redisKey);
        if(!StringUtils.isEmpty(serverId) && ProducerManager.getInstance().contains(serverId)){
            MessageQueueProducer producer = ProducerManager.getInstance()
                    .getServerProducer(serverId);

            producer.sendOrderMessage(getMessageInfo(data,serverId),data.getReceiveId().toString());
        }else{
            //消息逻辑
            log.info("system message receiver offLine:{}",data.getReceiveId());
            String offLineRedisKey = String.join(IMConstants.REDIS_KEY_SPLIT,
                    IMConstants.IM_USER_OFFLINE_MESSAGE, data.getReceiveId().toString());
            redisListService.leftPush(offLineRedisKey,JacksonUtil.to(getMutualInfo(data)));
        }

    }

    @Override
    public ImMessage persistenceData(InsideMessage insideMessage) {
        ImMessage imMessage = new ImMessage();
        imMessage.setContent(insideMessage.getData().toString());
        imMessage.setMessageType(MessageType.TEXT.code());
        imMessage.setRecvId(insideMessage.getReceiveId());
        imMessage.setSendId(insideMessage.getReceiveId());
        imMessage.setStatus(MessageStatus.SENDED.code());
        imMessage.setSendTime(LocalDateTime.now());
        imMessage.setCmdType(IMCmdType.INSIDE_MESSAGE.code());
        imMessage.setMessageCode(Long.parseLong(insideMessage.getMessageCode()));
        return imMessage;
    }

    private MessageInfo getMessageInfo(InsideMessage data,String serverId) {
        MutualInfo<InsideMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(serverId))
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    private MutualInfo<InsideMessage> getMutualInfo(InsideMessage data) {
        MutualInfo<InsideMessage> mutualInfo = new MutualInfo.Builder<InsideMessage>()
                .cmd(IMCmdType.INSIDE_MESSAGE.code())
                .info(data)
                .build();
        return mutualInfo;
    }

    @Override
    public InsideMessage transform(Object obj) {
        if(obj instanceof InsideMessage) return (InsideMessage)obj;
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new InsideMessage(), false);
    }
}
