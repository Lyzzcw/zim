package lyzzcw.work.zim.router.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.MutualInfo;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class SystemMessageProcessor implements MessageProcessor<SystemMessage> {

    @Resource
    private RedisDistributeCacheService distributeCacheService;
    @Autowired
    @Qualifier("persistenceProducer")
    private MessageQueueProducer persistenceProducer;
    @Resource
    private RedisListService redisListService;

    @Override
    public void process(SystemMessage data) {
        log.info("received system message:{}",data);
        //生成唯一消息码
        Long messageCode = SnowflakeIdWorker.generateId();
        data.setMessageCode(messageCode);
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
    public ImMessage persistenceData(SystemMessage SystemMessage) {
        ImMessage imMessage = new ImMessage();
        imMessage.setContent(SystemMessage.getData().toString());
        imMessage.setMessageType(MessageType.TEXT.code());
        imMessage.setRecvId(SystemMessage.getReceiveId());
        imMessage.setSendId(SystemMessage.getReceiveId());
        imMessage.setStatus(MessageStatus.SENDED.code());
        imMessage.setSendTime(LocalDateTime.now());
        imMessage.setCmdType(IMCmdType.SYSTEM_MESSAGE.code());
        imMessage.setMessageCode(SystemMessage.getMessageCode());
        return imMessage;
    }

    private MessageInfo getMessageInfo(SystemMessage data,String serverId) {
        MutualInfo<SystemMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(serverId))
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    private MutualInfo<SystemMessage> getMutualInfo(SystemMessage data) {
        MutualInfo<SystemMessage> mutualInfo = new MutualInfo.Builder<SystemMessage>()
                .cmd(IMCmdType.SYSTEM_MESSAGE.code())
                .info(data)
                .build();
        return mutualInfo;
    }

}
