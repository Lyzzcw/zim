package lyzzcw.work.zim.server.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.BeanUtils;
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


    @Override
    public void process(ChannelHandlerContext ctx, PrivateMessage data) {
        log.info("received private message:{}",data);
        responseWS(ctx,data);
    }

    private void responseWS(ChannelHandlerContext ctx,PrivateMessage data){
        // 响应WS的数据
        PrivateMessage response = new PrivateMessage();
        BeanUtils.copyProperties(data,response);
        response.setData("发送成功");
        MutualInfo<PrivateMessage> mutualInfo = new MutualInfo.Builder<PrivateMessage>()
                .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                .info(response)
                .build();
        ctx.channel().writeAndFlush(mutualInfo);
    }

    @Override
    public PrivateMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new PrivateMessage(), false);
    }
}
