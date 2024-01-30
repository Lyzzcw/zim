package lyzzcw.work.zim.server.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.AbstractMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.zim.server.cache.UserChannelContextCache;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import lyzzcw.work.zim.server.processor.factory.ProcessorFactory;
import lyzzcw.work.zim.server.util.SpringContextHolder;


/**
 * im channel 处理程序
 *
 * @author lzy
 * @date 2023/12/20
 */
@Slf4j
public class IMChannelHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage message) throws Exception {
        MessageProcessor processor = ProcessorFactory.getProcessor(IMCmdType.fromCode(message.getCmd()));
        processor.process(ctx, message);
    }

    /**
     * Netty中捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("IMChannelHandler.exceptionCaught|异常:{}", cause.getMessage());
    }

    /**
     * 用户终端与即时通讯后端服务建立连接后Netty回调的方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("IMChannelHandler.handlerAdded|{}连接", ctx.channel().id().asLongText());
    }

    /**
     * 用户终端与即时通讯后端服务断开连接时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(IMConstants.USER_ID);
        Long userId = ctx.channel().attr(userIdAttr).get();

        ChannelHandlerContext channelCtx = UserChannelContextCache.get(userId);
        // 防止异地登录误删
        if (channelCtx != null && channelCtx.channel().id().equals(ctx.channel().id())){
            UserChannelContextCache.remove(userId);
            DistributeCacheService distributeCacheService = SpringContextHolder.getBean(RedisDistributeCacheService.class);
            String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT, IMConstants.IM_USER_SERVER_ID, userId.toString());
            distributeCacheService.delete(redisKey);
            log.info("IMChannelHandler.handlerRemoved|断开连接, userId:{}", userId);
        }
    }

    /**
     * 用户事件触发
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE){
                AttributeKey<Long> attr = AttributeKey.valueOf(IMConstants.USER_ID);
                Long userId = ctx.channel().attr(attr).get();

                AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(IMConstants.TERMINAL_TYPE);
                Integer terminal = ctx.channel().attr(terminalAttr).get();
                log.info("IMChannelHandler.userEventTriggered|心跳超时.即将断开连接, userId:{}, 终端类型:{}", userId, terminal);
                ctx.channel().close();
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
