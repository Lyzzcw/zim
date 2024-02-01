package lyzzcw.work.zim.router.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.processor.factory.ProcessorFactory;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:03
 * Description: 消息路由侦听器
 */
@Component
@Slf4j
public class MessageRouteListener implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        list.forEach(
                messageExt -> {
                    try {
                        String msg = new String(messageExt.getBody());
                        log.info("Message Route Listener Received message:{}", msg);
                        //消息下发队列
                        MutualInfo mutualInfo = JSON.parseObject(msg, new TypeReference<MutualInfo>() {});
                        MessageProcessor<?> processor = ProcessorFactory.getProcessor(IMCmdType.fromCode(mutualInfo.getCmd()));
                        processor.process(processor.transform(mutualInfo.getInfo()));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        return ConsumeOrderlyStatus.SUCCESS;
    }


}
