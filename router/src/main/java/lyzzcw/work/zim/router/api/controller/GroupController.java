package lyzzcw.work.zim.router.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.domain.dto.GroupDTO;
import lyzzcw.work.zim.router.api.service.GroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 9:44
 * Description: No Description
 */
@RequestMapping("group")
@RestController
@Slf4j
@Api("群聊管理")
public class GroupController {

    @Resource
    private GroupService groupService;

    @PostMapping("create")
    @ApiOperation("新建群聊")
    public Result<Long> createGroup(@RequestBody GroupDTO groupDTO){
        return Result.ok(groupService.createGroup(groupDTO));
    }

    @PostMapping("invite")
    @ApiOperation("邀请入群")
    public Result<Long> invite(@RequestBody GroupDTO groupDTO){
        groupService.inviteGroupMember(groupDTO);
        return Result.ok();
    }
}
