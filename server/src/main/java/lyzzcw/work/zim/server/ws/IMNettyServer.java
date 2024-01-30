package lyzzcw.work.zim.server.ws;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/12/20 11:19
 * Description: im netty服务器
 */
public interface IMNettyServer {
    /**
     * 是否已就绪
     */
    boolean isReady();

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void shutdown();
}
