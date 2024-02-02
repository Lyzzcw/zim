package lyzzcw.work.zim.router.api.service;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 15:59
 * Description: No Description
 */
public interface SystemService {
    /**
     * 通知
     *
     * @param userId  用户 ID
     * @param message 消息
     * @return {@link Boolean}
     */
    Boolean notice(Long userId,String message);
}
