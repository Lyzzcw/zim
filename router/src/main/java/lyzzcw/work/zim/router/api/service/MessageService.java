package lyzzcw.work.zim.router.api.service;

import com.github.pagehelper.PageInfo;
import lyzzcw.work.zim.router.api.domain.dto.MessageDTO;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 15:03
 * Description: No Description
 */
public interface MessageService {
    PageInfo<ImMessage> page(MessageDTO messageDTO);
}
