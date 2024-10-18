package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageInfo;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.zim.server.cache.UserChannelContextCache;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 私聊消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class PrivateMessageProcessor implements MessageProcessor<PrivateMessage> {

    @Autowired
    @Qualifier("messageRouteProducer")
    private MessageQueueProducer messageQueueProducer;

    @Override
    public void process(ChannelHandlerContext ctx, PrivateMessage data) {
        log.info("received private message:{}",data);
        //封装信息 扔到mq中
        MessageInfo messageInfo = getMessageInfo(data);
        messageQueueProducer.sendOrderMessage(messageInfo,data.getReceiveId().toString());
        //返回客户端发送结果
//        if(data.getSendResult()){
//            responseWS(ctx,data);
//        }
    }

    @Override
    public boolean OfflineMessageProcess(ChannelHandlerContext ctx,MutualInfo info){
        log.info("send offline message:{}",info);
        return ctx.writeAndFlush(info).isSuccess();
    }

    private MessageInfo getMessageInfo(PrivateMessage data) {
        MutualInfo<PrivateMessage> mutualInfo = getMutualInfo(data);
        MessageInfo messageInfo = new MessageInfo.Builder()
                .topic(MQConstants.MESSAGE_TO_ROUTE_TOPIC)
                .body(JacksonUtil.to(mutualInfo))
                .build();
        return messageInfo;
    }

    private MutualInfo<PrivateMessage> getMutualInfo(PrivateMessage data) {
        return new MutualInfo.Builder<PrivateMessage>()
                    .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                    .info(data)
                    .build();
    }

    private void responseWS(ChannelHandlerContext ctx,PrivateMessage data){
        // 响应WS的数据
        ctx.channel().writeAndFlush(getMutualInfo(data));
    }

    @Override
    public PrivateMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new PrivateMessage(), false);
    }

    @Override
    public void sendToReceiver(PrivateMessage data){
        ChannelHandlerContext ctx = UserChannelContextCache.get(data.getReceiveId());
        if(ctx != null){
            ctx.channel().writeAndFlush(getMutualInfo(data));
        }else {
            log.info("send to user({}) offline",data.getReceiveId());
        }
    }
}
