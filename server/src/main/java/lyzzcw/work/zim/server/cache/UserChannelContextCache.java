package lyzzcw.work.zim.server.cache;


import io.netty.channel.ChannelHandlerContext;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户通道上下文缓存
 *
 * @author lzy
 * @date 2023/12/20
 */
public class UserChannelContextCache {

    private static Map<Long, ChannelHandlerContext> connectionMap = new ConcurrentHashMap<>();
    private static volatile UserChannelContextCache instance;
    private UserChannelContextCache(){}
    /**
     * 单例模式
     */
    public static UserChannelContextCache getInstance(){
        if (instance == null){
            synchronized (UserChannelContextCache.class){
                if (instance == null){
                    instance = new UserChannelContextCache();
                }
            }
        }
        return instance;
    }

    /**
     * 添加连接
     */
    public static void add(Long userId,ChannelHandlerContext ctx){
        connectionMap.put(userId,ctx);
    }

    /**
     * 移除连接
     */
    public static void remove(Long userId){
        connectionMap.remove(userId);
    }
    /**
     * 获取连接
     */
    public static ChannelHandlerContext get(Long userId){
        return connectionMap.get(userId);
    }
}
