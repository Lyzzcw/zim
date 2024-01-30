package lyzzcw.work.zim.server.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lyzzcw.work.common.domain.AbstractMessage;


import java.util.List;


/**
 * Web 套接字消息协议解码器
 *
 * @author lzy
 * @date 2023/12/20
 */
public class WebSocketMessageProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AbstractMessage imSendInfo = objectMapper.readValue(textWebSocketFrame.text(), AbstractMessage.class);
        list.add(imSendInfo);
    }
}
