package lyzzcw.work.zim.router.rocketmq;

import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueConsumer;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueConsumerImpl;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueProducerImpl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 11:38
 * Description: No Description
 */
@Configuration
public class RocketMQConfig {
    @Value("${rocketmq.namesvr}")
    private String namesvr;

    @Resource
    private MessageRouteListener messageRouteListener;
    @Resource
    private MessagePersistenceListener messagePersistenceListener;

    @Bean("messageRouteConsumer")
    public MessageQueueConsumer messageRouteConsumer() throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setTopic(MQConstants.MESSAGE_TO_ROUTE_TOPIC);
        config.setGroup(MQConstants.MESSAGE_TO_ROUTE_GROUP);
        MessageQueueConsumer consumer = new MessageQueueConsumerImpl();
        consumer.initOrderConsumer(config,messageRouteListener);
        return consumer;
    }

    @Bean("persistenceProducer")
    public MessageQueueProducer persistenceProducer() throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setGroup(MQConstants.MESSAGE_PERSISTENCE_GROUP);
        config.setTopic(MQConstants.MESSAGE_PERSISTENCE_TOPIC);
        MessageQueueProducer producer = new MessageQueueProducerImpl();
        producer.initProducer(config);
        return producer;
    }

    @Bean("persistenceConsumer")
    public MessageQueueConsumer persistenceConsumer() throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setGroup(MQConstants.MESSAGE_PERSISTENCE_GROUP);
        config.setTopic(MQConstants.MESSAGE_PERSISTENCE_TOPIC);
        MessageQueueConsumer consumer = new MessageQueueConsumerImpl();
        consumer.initConsumer(config,messagePersistenceListener);
        return consumer;
    }
}
