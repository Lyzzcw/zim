package lyzzcw.work.zim.router.api.service;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 15:59
 * Description: No Description
 */
public interface InsideService {

    /**
     * 发送
     *
     * @param userId  用户 ID
     * @param message 消息
     * @return {@link Boolean}
     */
    Boolean send(Long userId,String message);
}
