package lyzzcw.work.zim.server.processor;

import io.netty.channel.ChannelHandlerContext;
import lyzzcw.work.common.domain.AbstractMessage;

/**
 * 消息处理器
 *
 * @author lzy
 * @date 2023/12/20
 */
public interface MessageProcessor {

    /**
     * 处理数据
     */
    default void process(ChannelHandlerContext ctx, AbstractMessage<?> message){

    }

}
