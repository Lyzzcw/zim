package lyzzcw.work.zim.router.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.service.InsideService;
import lyzzcw.work.zim.router.api.service.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 8:54
 * Description: No Description
 */
@RestController
@RequestMapping("inside")
@Slf4j
@Api("站内管理")
public class InsideController {

    @Resource
    private InsideService insideService;

    @GetMapping("send")
    @ApiOperation("站内信")
    public Result<Boolean> send(@RequestParam("userId") Long userId,
                                 @RequestParam("message") String message){
        return Result.ok(insideService.send(userId, message));
    }
}
