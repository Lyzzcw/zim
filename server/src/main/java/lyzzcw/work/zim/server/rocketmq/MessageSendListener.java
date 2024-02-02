package lyzzcw.work.zim.server.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import lyzzcw.work.zim.server.processor.factory.ProcessorFactory;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:03
 * Description: 消息发送侦听器
 */
@Slf4j
public class MessageSendListener implements MessageListenerOrderly {
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        list.forEach(
                messageExt -> {
                    MutualInfo mutualInfo = JSON.parseObject(new String(messageExt.getBody())
                            , new TypeReference<MutualInfo>() {
                            });
                    log.info("message send listener :{}",mutualInfo);
                    MessageProcessor<?> processor = ProcessorFactory.getProcessor(Objects.requireNonNull(IMCmdType.fromCode(mutualInfo.getCmd())));
                    Assert.notNull(processor, "not available processor for message");
                    processor.sendToReceiver(processor.transform(mutualInfo.getInfo()));
                }
        );
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
