package lyzzcw.work.zim.router.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.router.api.domain.dto.GroupDTO;
import lyzzcw.work.zim.router.api.service.GroupService;
import lyzzcw.work.zim.router.infrastructure.entity.ImGroup;
import lyzzcw.work.zim.router.infrastructure.entity.ImGroupMember;
import lyzzcw.work.zim.router.infrastructure.entity.ImUser;
import lyzzcw.work.zim.router.infrastructure.mapper.ImGroupMapper;
import lyzzcw.work.zim.router.infrastructure.mapper.ImGroupMemberMapper;
import lyzzcw.work.zim.router.infrastructure.mapper.ImUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 9:46
 * Description: No Description
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Resource
    private ImGroupMapper imGroupMapper;
    @Resource
    private ImUserMapper imUserMapper;
    @Resource
    private ImGroupMemberMapper imGroupMemberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGroup(GroupDTO groupDTO) {
        //insert group
        ImGroup imGroup = new ImGroup();
        imGroup.setName(groupDTO.getGroupName());
        imGroup.setOwnerId(groupDTO.getCreator());
        imGroup.setCreatedTime(LocalDateTime.now());
        imGroup.setDeleted(0);
        imGroupMapper.insert(imGroup);
        //insert group member
        List<ImUser> users = imUserMapper.selectByList(groupDTO.getInvitees());
        List<ImGroupMember> groups = users.stream()
                .map(user -> {
                    ImGroupMember imGroupMember = new ImGroupMember();
                    imGroupMember.setUserId(user.getId());
                    imGroupMember.setAliasName(user.getUserName());
                    imGroupMember.setGroupId(imGroup.getId());
                    imGroupMember.setCreatedTime(LocalDateTime.now());
                    imGroupMember.setQuit(0);
                    return imGroupMember;
                }).collect(Collectors.toList());
        imGroupMemberMapper.batchInsert(groups);
        return imGroup.getId();
    }

    @Override
    public void inviteGroupMember(GroupDTO groupDTO) {
        ImUser user = imUserMapper.selectByPrimaryKey(groupDTO.getInvitees().get(0));
        ImGroupMember imGroupMember = new ImGroupMember();
        imGroupMember.setUserId(user.getId());
        imGroupMember.setAliasName(user.getUserName());
        imGroupMember.setGroupId(groupDTO.getId());
        imGroupMember.setCreatedTime(LocalDateTime.now());
        imGroupMember.setQuit(0);
        imGroupMemberMapper.insert(imGroupMember);
    }
}
