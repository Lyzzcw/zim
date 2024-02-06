package lyzzcw.work.zim.router.api.service;

import lyzzcw.work.zim.router.api.domain.dto.UserDTO;
import lyzzcw.work.zim.router.infrastructure.entity.ImUser;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 9:14
 * Description: No Description
 */
public interface LoginService {
    String discovery();

    ImUser register(UserDTO userDTO);
}
