package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.InsideMessage;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.zim.server.cache.UserChannelContextCache;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 系统消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class InsideMessageProcessor implements MessageProcessor<InsideMessage> {

    @Autowired
    @Qualifier("messageRouteProducer")
    private MessageQueueProducer messageQueueProducer;

    @Override
    public void process(ChannelHandlerContext ctx, InsideMessage data) {
        log.info("received inside message:{}",data);
        messageQueueProducer.sendMessage(getMessageInfo(data));
    }

    @Override
    public boolean OfflineMessageProcess(ChannelHandlerContext ctx,MutualInfo info){
        log.info("send offline message:{}",info);
        return ctx.writeAndFlush(info).isSuccess();
    }

    private MutualInfo<InsideMessage> getMutualInfo(InsideMessage data) {
        return new MutualInfo.Builder<InsideMessage>()
                    .cmd(IMCmdType.INSIDE_MESSAGE.code())
                    .info(data)
                    .build();
    }

    private MessageInfo getMessageInfo(InsideMessage data) {
        MutualInfo<InsideMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_TO_ROUTE_TOPIC)
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    @Override
    public InsideMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new InsideMessage(), false);
    }

    @Override
    public void sendToReceiver(InsideMessage data){
        ChannelHandlerContext ctx = UserChannelContextCache.get(data.getReceiveId());
        if(ctx != null){
            ctx.channel().writeAndFlush(getMutualInfo(data));
        }else {
            log.info("send to user({}) offline",data.getReceiveId());
        }
    }
}
