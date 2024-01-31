package lyzzcw.work.common.rocketmq.service.impl;

import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 11:16
 * Description: No Description
 */
public class MessageQueueProducerImpl implements MessageQueueProducer {

    private DefaultMQProducer producer;


    @Override
    public void initProducer(MessageQueueConfig messageQueueConfig) throws MQClientException {
        this.producer = new DefaultMQProducer(messageQueueConfig.getGroup());
        this.producer.setNamesrvAddr(messageQueueConfig.getNamesvr());
        this.producer.start();
    }


    @Override
    public SendResult sendMessage(MessageInfo messageInfo){
        try {
            SendResult sendResult = this.producer.send(messageInfo);
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SendResult sendOrderMessage(MessageInfo messageInfo, String orderId) {
        try {
            SendResult sendResult = this.producer.send(messageInfo, new SelectMessageQueueByHash(), orderId);
            return sendResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void shutdown() {
        this.producer.shutdown();
    }
}
