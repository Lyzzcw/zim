package lyzzcw.work.zim.router.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:03
 * Description: 消息路由侦听器
 */
public class MessageRouteListener implements MessageListenerOrderly {
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        list.forEach(
                messageExt -> {
                    String msg = JSON.parseObject(new String(messageExt.getBody())
                            , new TypeReference<String>() {
                            });
                    System.out.println(msg);
                }
        );
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
