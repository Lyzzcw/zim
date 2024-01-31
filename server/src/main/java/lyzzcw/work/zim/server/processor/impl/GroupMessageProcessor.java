package lyzzcw.work.zim.server.processor.impl;


import cn.hutool.core.bean.BeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.GroupMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.server.processor.MessageProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 群消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class GroupMessageProcessor implements MessageProcessor<GroupMessage>{


    @Async
    @Override
    public void process(ChannelHandlerContext ctx, GroupMessage data) {
        log.info("received group message:{}",data);
        responseWS(ctx,data);
    }

    private void responseWS(ChannelHandlerContext ctx, GroupMessage data){
        // 响应WS的数据
        GroupMessage response = new GroupMessage();
        BeanUtils.copyProperties(data,response);
        response.setData("发送成功");
        MutualInfo<GroupMessage> mutualInfo = new MutualInfo.Builder<GroupMessage>()
                .cmd(IMCmdType.GROUP_MESSAGE.code())
                .info(response)
                .build();
        ctx.channel().writeAndFlush(mutualInfo);
    }

    @Override
    public GroupMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new GroupMessage(), false);
    }
}
