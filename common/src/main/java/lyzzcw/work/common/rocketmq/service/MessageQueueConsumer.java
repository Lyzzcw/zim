package lyzzcw.work.common.rocketmq.service;

import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 11:21
 * Description: No Description
 */
public interface MessageQueueConsumer {

    /**
     * init 使用者
     *
     * @param messageQueueConfig          消息队列配置
     * @param messageListenerConcurrently 消息侦听器并发
     * @throws MQClientException MQClient异常
     */
    void initConsumer(MessageQueueConfig messageQueueConfig,
                      MessageListenerConcurrently messageListenerConcurrently) throws MQClientException;

    /**
     * 初始化顺序使用者
     *
     * @param messageQueueConfig     消息队列配置
     * @param messageListenerOrderly 消息侦听器有序
     * @throws MQClientException MQClient异常
     */
    void initOrderConsumer(MessageQueueConfig messageQueueConfig,
                           MessageListenerOrderly messageListenerOrderly) throws MQClientException;

    /**
     * 关闭
     */
    void shutdown();
}
