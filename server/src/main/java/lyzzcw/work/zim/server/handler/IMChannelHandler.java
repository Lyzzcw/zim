package lyzzcw.work.zim.server.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.zim.server.cache.UserChannelContextCache;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import lyzzcw.work.zim.server.processor.factory.ProcessorFactory;
import lyzzcw.work.zim.server.processor.impl.LoginProcessor;
import lyzzcw.work.zim.server.util.SpringContextHolder;
import org.springframework.util.Assert;

import java.util.Objects;


/**
 * im channel 处理程序
 *
 * @author lzy
 * @date 2023/12/20
 */
@Slf4j
public class IMChannelHandler extends SimpleChannelInboundHandler<MutualInfo> {

    //系统广播
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MutualInfo mutualInfo) throws Exception {
        MessageProcessor<?> processor = ProcessorFactory.getProcessor(Objects.requireNonNull(IMCmdType.fromCode(mutualInfo.getCmd())));
        Assert.notNull(processor, "not available processor for message");
        // 登录校验
        if(!processor.getClass().equals(LoginProcessor.class)){
            AttributeKey<Long> userIdAttr = AttributeKey.valueOf(IMConstants.USER_ID);
            Long userId = ctx.channel().attr(userIdAttr).get();
            if(Objects.isNull(userId)){
                log.warn("current channel need to login first:{}",ctx.channel().id().asLongText());
                ctx.close();
                return;
            }
        }
        processor.process(ctx, processor.transform(mutualInfo.getInfo()));
    }

    /**
     * Netty中捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("IMChannelHandler.exceptionCaught|异常:{}", cause.getMessage());
//        this.userOffline(ctx);
    }

    /**
     * 用户终端与即时通讯后端服务建立连接后Netty回调的方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("IMChannelHandler.handlerAdded|{}连接", ctx.channel().id().asLongText());
        channels.add(ctx.channel());
        MutualInfo<String> mutualInfo = new MutualInfo.Builder<String>()
                .cmd(IMCmdType.SYSTEM_MESSAGE.code())
                .info(ctx.channel().id().asLongText()+",登录成功")
                .build();
        channels.writeAndFlush(mutualInfo);
    }

    /**
     * 用户终端与即时通讯后端服务断开连接时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(log.isDebugEnabled()){
            log.debug("IMChannelHandler.handlerRemoved|{}离线",ctx.channel().id().asLongText());
        }
        this.userOffline(ctx);
    }

    /**
     * 用户离线
     *
     * @param ctx    CTX公司
     */
    private void userOffline(ChannelHandlerContext ctx) {
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(IMConstants.USER_ID);
        Long userId = ctx.channel().attr(userIdAttr).get();
        ChannelHandlerContext channelCtx = UserChannelContextCache.get(userId);
        log.error("user offline : {} |current ctx:{},system ctx:{}",
                userId,ctx.channel().id().asLongText(),channelCtx.channel().id().asLongText());
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
