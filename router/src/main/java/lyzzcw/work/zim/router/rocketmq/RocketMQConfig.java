package lyzzcw.work.zim.router.rocketmq;

import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueConsumer;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueConsumerImpl;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueProducerImpl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean("messageRouteConsumer")
    public MessageQueueConsumer messageRouteProducer() throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setTopic(MQConstants.MESSAGE_TO_ROUTE_TOPIC);
        config.setGroup(MQConstants.MESSAGE_TO_ROUTE_GROUP);
        MessageQueueConsumer consumer = new MessageQueueConsumerImpl();
        consumer.initOrderConsumer(config,new MessageRouteListener());
        return consumer;
    }
}
