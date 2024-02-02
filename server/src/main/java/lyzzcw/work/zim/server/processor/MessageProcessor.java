package lyzzcw.work.zim.server.processor;

import io.netty.channel.ChannelHandlerContext;
import lyzzcw.work.common.domain.AbstractMessage;
import lyzzcw.work.common.domain.MutualInfo;

/**
 * 消息处理器
 *
 * @author lzy
 * @date 2023/12/20
 */
public interface MessageProcessor<T> {

    /**
     * 处理数据
     */
    default void process(ChannelHandlerContext ctx, T info){

    }

    default boolean OfflineMessageProcess(ChannelHandlerContext ctx,MutualInfo info){
        return true;
    }

    default <T> T transform(Object obj){
        return (T)obj;
    }

    default void sendToReceiver(T info){

    }
}
