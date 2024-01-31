package lyzzcw.work.zim.router.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lyzzcw.work.common.domain.MutualInfo;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:03
 * Description: 消息路由侦听器
 */
@Component
public class MessageRouteListener implements MessageListenerOrderly {
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        list.forEach(
                messageExt -> {
                    String msg = new String(messageExt.getBody());
                    MutualInfo mutualInfo = JSON.parseObject(msg, new TypeReference<MutualInfo>() {
                            });
                    System.out.println(mutualInfo);
                }
        );
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
