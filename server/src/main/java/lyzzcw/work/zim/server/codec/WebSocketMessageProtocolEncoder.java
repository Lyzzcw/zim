package lyzzcw.work.zim.server.codec;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.AbstractMessage;
import lyzzcw.work.common.domain.MutualInfo;

import java.util.List;


/**
 * Web 套接字消息协议编码器
 *
 * @author lzy
 * @date 2023/12/20
 */
@Slf4j
public class WebSocketMessageProtocolEncoder extends MessageToMessageEncoder<MutualInfo> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MutualInfo mutualInfo, List<Object> list) throws Exception {
        JsonFactory jsonFactory = JsonFactory.builder()
//                .configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS, true)// 配置特性
                .build();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        String text = objectMapper.writeValueAsString(mutualInfo);
        if(log.isDebugEnabled()) {
            log.debug("send message: {}/{}" ,channelHandlerContext.channel().id(), text);
        }
        TextWebSocketFrame frame = new TextWebSocketFrame(text);
        list.add(frame);
    }
}
