package lyzzcw.work.zim.router.rocketmq;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.service.ImMessageService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:03
 * Description: 消息路由侦听器
 */
@Component
@Slf4j
public class MessagePersistenceListener implements MessageListenerConcurrently {

    @Resource
    private ImMessageService imPrivateMessageService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        list.forEach(
                messageExt -> {
                    try {
                        String msg = new String(messageExt.getBody());
                        log.info("Message persistence Listener Received message:{}", msg);
                        //消息持久化
                        ImMessage imMessage = JacksonUtil.from(msg, ImMessage.class);
                        imPrivateMessageService.persistent(imMessage);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
