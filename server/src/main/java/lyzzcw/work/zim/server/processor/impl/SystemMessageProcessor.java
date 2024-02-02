package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.SystemMessage;
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
public class SystemMessageProcessor implements MessageProcessor<SystemMessage> {

    @Autowired
    @Qualifier("messageRouteProducer")
    private MessageQueueProducer messageQueueProducer;

    @Override
    public void process(ChannelHandlerContext ctx, SystemMessage data) {
        //none of the methods
    }

    @Override
    public boolean OfflineMessageProcess(ChannelHandlerContext ctx,MutualInfo info){
        log.info("send offline message:{}",info);
        return ctx.writeAndFlush(info).isSuccess();
    }

    private MutualInfo<SystemMessage> getMutualInfo(SystemMessage data) {
        return new MutualInfo.Builder<SystemMessage>()
                    .cmd(IMCmdType.SYSTEM_MESSAGE.code())
                    .info(data)
                    .build();
    }

    @Override
    public SystemMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new SystemMessage(), false);
    }

    @Override
    public void sendToReceiver(SystemMessage data){
        ChannelHandlerContext ctx = UserChannelContextCache.get(data.getReceiveId());
        if(ctx != null){
            ctx.channel().writeAndFlush(getMutualInfo(data));
        }else {
            log.info("send to user({}) offline",data.getReceiveId());
        }
    }
}
