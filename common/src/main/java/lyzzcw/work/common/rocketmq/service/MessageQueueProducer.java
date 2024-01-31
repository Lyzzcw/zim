package lyzzcw.work.common.rocketmq.service;

import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 10:55
 * Description: No Description
 */
public interface MessageQueueProducer {

    /**
     * 初始化生产者
     *
     * @param messageQueueConfig 消息队列配置
     * @throws MQClientException MQClient异常
     */
    void initProducer(MessageQueueConfig messageQueueConfig) throws MQClientException;


    /**
     * 发送短消息
     *
     * @param messageInfo 留言信息
     * @return {@link SendResult}
     */
    SendResult sendMessage(MessageInfo messageInfo);

    /**
     * 发送顺序消息
     *
     * @param messageInfo 留言信息
     * @param orderId     顺序编号
     * @return {@link SendResult}
     */
    SendResult sendOrderMessage(MessageInfo messageInfo,String orderId);

    /**
     * 关闭
     */
    void shutdown();
}
