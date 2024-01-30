package lyzzcw.work.common.rocketmq.service.impl;

import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 11:27
 * Description: No Description
 */
public class MessageQueueConsumerImpl implements MessageQueueConsumer {
    private DefaultMQPushConsumer consumer;
    @Override
    public void initConsumer(MessageQueueConfig messageQueueConfig,
                             MessageListenerConcurrently messageListenerConcurrently) throws MQClientException {
        this.consumer = new DefaultMQPushConsumer(messageQueueConfig.getGroup());
        consumer.setNamesrvAddr(messageQueueConfig.getNamesvr());
        consumer.subscribe(messageQueueConfig.getTopic(),messageQueueConfig.getTag());
        consumer.registerMessageListener(messageListenerConcurrently);
        consumer.start();
    }

    @Override
    public void initOrderConsumer(MessageQueueConfig messageQueueConfig, MessageListenerOrderly messageListenerOrderly) throws MQClientException {
        this.consumer = new DefaultMQPushConsumer(messageQueueConfig.getGroup());
        consumer.setNamesrvAddr(messageQueueConfig.getNamesvr());
        consumer.subscribe(messageQueueConfig.getTopic(),messageQueueConfig.getTag());
        consumer.registerMessageListener(messageListenerOrderly);
        consumer.start();
    }

    @Override
    public void shutdown() {
        this.consumer.shutdown();
    }
}
