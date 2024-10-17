package lyzzcw.work.zim.router.service;

import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/1 9:31
 * Description: No Description
 */
public interface ImMessageService {

    /**
     * 消息入库
     *
     * @param imMessage IM 消息
     * @return {@link Integer}
     */
    Integer persistent(ImMessage imMessage);

    /**
     * 通过代码更新消息
     *
     * @param imMessage IM 消息
     */
    void updateMessageByCode(ImMessage imMessage);
}
