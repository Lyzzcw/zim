package lyzzcw.work.zim.router.api.controller;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.service.LoginService;
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
@RequestMapping("system")
@Slf4j
public class SystemController {

    @Resource
    private SystemService systemService;

    @GetMapping("notice")
    public Result<Boolean> notice(@RequestParam("userId") Long userId,
                                 @RequestParam("message") String message){
        return Result.ok(systemService.notice(userId, message));
    }
}
