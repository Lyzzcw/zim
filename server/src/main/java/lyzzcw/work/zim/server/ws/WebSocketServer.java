package lyzzcw.work.zim.server.ws;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.server.codec.WebSocketMessageProtocolDecoder;
import lyzzcw.work.zim.server.codec.WebSocketMessageProtocolEncoder;
import lyzzcw.work.zim.server.handler.IMChannelHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * Web 套接字服务器
 *
 * @author lzy
 * @date 2023/12/20
 */
@Component
@ConditionalOnProperty(prefix = "websocket", value = "enable",
        havingValue = "true",matchIfMissing = true)
@Slf4j
public class WebSocketServer implements IMNettyServer {

    private volatile boolean ready = false;

    @Value("${websocket.port}")
    private int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    // 添加处理的Handler，通常包括消息编解码、业务处理，也可以是日志、权限、过滤等
                    @Override
                    protected void initChannel(Channel ch) {
                        // 获取职责链
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(300, 0, 0, TimeUnit.SECONDS));
                        pipeline.addLast("http-codec", new HttpServerCodec());
                        pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
                        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                        pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                        pipeline.addLast("encode",  new WebSocketMessageProtocolEncoder());
                        pipeline.addLast("decode", new WebSocketMessageProtocolDecoder());
                        pipeline.addLast("handler", new IMChannelHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 5)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            bootstrap.bind(port).sync().channel();
            this.ready = true;
            log.info("WebSocketServer.start|websocket server 初始化完成,端口：{}", port);
        } catch (InterruptedException e) {
            log.info("WebSocketServer.start|websocket server 初始化异常", e);
        }
    }

    @Override
    public void shutdown() {
        if(bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown() ) {
            bossGroup.shutdownGracefully();
        }
        if(workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown() ) {
            workGroup.shutdownGracefully();
        }
        this.ready = false;
        log.info("WebSocketServer.shutdown|websocket server 停止");
    }
}
