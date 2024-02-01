package lyzzcw.work.zim.router.rocketmq;

import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueProducerImpl;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/31 17:03
 * Description: No Description
 */
public class RocketMQUtil {

    /**
     * 消息发送生产者
     *
     * @param namesvr namesvr
     * @param id      编号
     * @return {@link MessageQueueProducer}
     * @throws MQClientException MQClient异常
     */
    public static MessageQueueProducer messageSendProducer(String namesvr,String id) throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setTopic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(id));
        config.setGroup(MQConstants.MESSAGE_FROM_ROUTE_GROUP.concat(id));
        MessageQueueProducer producer = new MessageQueueProducerImpl();
        producer.initProducer(config);
        return producer;
    }

    /**
     * 构建持久性消息
     *
     * @param msg 味精
     * @return {@link MessageInfo}
     */
    public static MessageInfo buildPersistenceMessage(String msg){
        MessageInfo messageInfo = new MessageInfo.Builder()
                .body(msg)
                .topic(MQConstants.MESSAGE_PERSISTENCE_TOPIC)
                .build();
        return messageInfo;
    }
}
