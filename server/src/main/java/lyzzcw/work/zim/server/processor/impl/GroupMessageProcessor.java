package lyzzcw.work.zim.server.processor.impl;


import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import lyzzcw.work.common.domain.AbstractMessage;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 群消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class GroupMessageProcessor implements MessageProcessor{


    @Async
    @Override
    public void process(ChannelHandlerContext ctx, AbstractMessage message) {

    }

}
