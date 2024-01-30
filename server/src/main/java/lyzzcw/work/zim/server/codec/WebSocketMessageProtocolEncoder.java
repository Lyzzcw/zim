package lyzzcw.work.zim.server.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lyzzcw.work.common.domain.AbstractMessage;

import java.util.List;


/**
 * Web 套接字消息协议编码器
 *
 * @author lzy
 * @date 2023/12/20
 */
public class WebSocketMessageProtocolEncoder extends MessageToMessageEncoder<AbstractMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractMessage imSendInfo, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String text = objectMapper.writeValueAsString(imSendInfo);
        TextWebSocketFrame frame = new TextWebSocketFrame(text);
        list.add(frame);
    }
}
