package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.HeartbeatMessage;
import lyzzcw.work.common.domain.LoginMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.zim.server.cache.UserChannelContextCache;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class LoginProcessor implements MessageProcessor<LoginMessage> {


    @Value("${server.id}")
    private String serverId;

    @Autowired
    private DistributeCacheService distributedCacheService;

    @Override
    public synchronized void process(ChannelHandlerContext ctx, LoginMessage data) {
        //登录Token检验未通过
        if (data == null) {
            log.warn("LoginProcessor.process|转化后的loginInfo为空");
            return;
        }
        Long userId = data.getUserId();
        log.info("LoginProcessor.process|用户登录, userId:{}", userId);
        ChannelHandlerContext channelCtx = UserChannelContextCache.get(userId);
        //判断当前连接的id不同，则表示当前用户已经在异地登录
        if (channelCtx != null && !channelCtx.channel().id().equals(ctx.channel().id())) {
            //不允许用户在同一种终端，登录多个设备
            LoginMessage loginMessage = new LoginMessage.Builder()
                    .cmd(IMCmdType.FORCE_LOGUT.code())
                    .sendResult(Boolean.FALSE)
                    .userId(userId)
                    .data("您已在其他地方登录，将被强制下线")
                    .build();
            this.responseWS(ctx,loginMessage);
            log.info("LoginProcessor.process|异地登录，强制下线，userid:{}", userId);
        }
        //缓存用户和Channel的关系
        UserChannelContextCache.add(userId, ctx);
        //设置用户相关的属性
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(IMConstants.USER_ID);
        ctx.channel().attr(userIdAttr).set(userId);

        //初始化心跳的次数
        AttributeKey<Long> heartbeatAttr = AttributeKey.valueOf(IMConstants.HEARTBEAT_TIMES);
        ctx.channel().attr(heartbeatAttr).set(0L);

        //记录用户的channelId
        String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT, IMConstants.IM_USER_SERVER_ID, userId.toString());
        distributedCacheService.set(redisKey, serverId, IMConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        //响应ws
        LoginMessage loginMessage = new LoginMessage.Builder()
                .cmd(IMCmdType.LOGIN.code())
                .userId(userId)
                .data("登录成功")
                .build();
        this.responseWS(ctx,loginMessage);
    }

    /**
     * 响应ws的数据
     */
    private void responseWS(ChannelHandlerContext ctx,LoginMessage loginMessage) {
        // 响应WS的数据
        MutualInfo<LoginMessage> mutualInfo = new MutualInfo.Builder<LoginMessage>()
                .cmd(IMCmdType.LOGIN.code())
                .info(loginMessage)
                .build();
        ctx.channel().writeAndFlush(mutualInfo);
    }

    @Override
    public LoginMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new LoginMessage(), false);
    }

}
