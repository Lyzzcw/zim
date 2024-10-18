package lyzzcw.work.zim.server.codec;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.AbstractMessage;
import lyzzcw.work.common.domain.MutualInfo;


import java.util.List;


/**
 * Web 套接字消息协议解码器
 *
 * @author lzy
 * @date 2023/12/20
 */
@Slf4j
public class WebSocketMessageProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        String text = textWebSocketFrame.text();
        if(log.isDebugEnabled()) {
            log.debug("receive message: {}/{}" ,channelHandlerContext.channel().id(),text);
        }
        JsonFactory jsonFactory = JsonFactory.builder()
//                .configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS, true)// 配置特性
                .build();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        MutualInfo<?> mutualInfo = objectMapper.readValue(text, MutualInfo.class);
        list.add(mutualInfo);
    }
}
