package lyzzcw.work.zim.router.api.controller;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.service.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 8:54
 * Description: No Description
 */
@RestController
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("discovery")
    public Result<String> discovery(){
        return Result.ok(loginService.discovery(),"success");
    }
}
