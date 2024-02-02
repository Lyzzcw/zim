package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.constants.IMConstants;
import lyzzcw.work.common.domain.HeartbeatMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 心跳处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class HeartbeatProcessor implements MessageProcessor<HeartbeatMessage> {
    @Autowired
    private DistributeCacheService distributedCacheService;

    @Value("${websocket.heartbeat.count}")
    private Integer heartbeatCount;

    @Override
    public void process(ChannelHandlerContext ctx, HeartbeatMessage data) {
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(IMConstants.USER_ID);
        Long userId = ctx.channel().attr(userIdAttr).get();
        if(log.isDebugEnabled()){
            log.debug("HeartbeatProcessor|process,用户:{}上报心跳",userId);
        }
        //响应ws
        this.responseWS(ctx,userId);
        //设置属性
        AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(IMConstants.HEARTBEAT_TIMES);
        Long heartbeatTimes = ctx.channel().attr(heartBeatAttr).get();
        ctx.channel().attr(heartBeatAttr).set(++heartbeatTimes);
        if (heartbeatTimes % heartbeatCount == 0) {
            //心跳10次，用户在线状态续命一次
            if(log.isDebugEnabled()){
                log.debug("HeartbeatProcessor|process,用户:{}在线状态续命",userId);
            }
            String redisKey = String.join(IMConstants.REDIS_KEY_SPLIT, IMConstants.IM_USER_SERVER_ID, userId.toString());
            distributedCacheService.expire(redisKey, IMConstants.ONLINE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
    }

    /**
     * 响应ws的数据
     */
    private void responseWS(ChannelHandlerContext ctx,Long userId) {
        // 响应WS的数据
        HeartbeatMessage heartbeatMessage = new HeartbeatMessage();
        heartbeatMessage.setCmd(IMCmdType.HEART_BEAT.code());
        heartbeatMessage.setData("pong");
        heartbeatMessage.setUserId(userId);
        MutualInfo<HeartbeatMessage> mutualInfo = getMutualInfo(heartbeatMessage);
        ctx.channel().writeAndFlush(mutualInfo);
    }

    private MutualInfo<HeartbeatMessage> getMutualInfo(HeartbeatMessage heartbeatMessage) {
        MutualInfo<HeartbeatMessage> mutualInfo = new MutualInfo.Builder<HeartbeatMessage>()
                .cmd(IMCmdType.HEART_BEAT.code())
                .info(heartbeatMessage)
                .build();
        return mutualInfo;
    }

    @Override
    public HeartbeatMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new HeartbeatMessage(), false);
    }
}
