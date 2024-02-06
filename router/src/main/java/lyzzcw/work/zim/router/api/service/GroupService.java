package lyzzcw.work.zim.router.api.service;

import lyzzcw.work.zim.router.api.domain.dto.GroupDTO;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 9:46
 * Description: No Description
 */
public interface GroupService {
    /**
     * 创建组
     *
     * @param groupDTO DTO 组
     * @return {@link Long}
     */
    Long createGroup(GroupDTO groupDTO);

    /**
     * 邀请群组成员
     *
     * @param groupDTO DTO 组
     */
    void inviteGroupMember(GroupDTO groupDTO);
}
