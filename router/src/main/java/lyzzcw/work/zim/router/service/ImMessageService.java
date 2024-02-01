package lyzzcw.work.zim.router.service;

import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/1 9:31
 * Description: No Description
 */
public interface ImMessageService {
    Integer persistent(ImMessage imMessage);
}
