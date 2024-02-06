package lyzzcw.work.zim.router.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.domain.dto.UserDTO;
import lyzzcw.work.zim.router.api.service.LoginService;
import lyzzcw.work.zim.router.infrastructure.entity.ImUser;
import org.springframework.web.bind.annotation.*;

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
@Api("登录管理")
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("discovery")
    @ApiOperation("服务发现")
    public Result<String> discovery(){
        return Result.ok(loginService.discovery(),"success");
    }

    @PostMapping("register")
    @ApiOperation("注册")
    public Result<ImUser> register(@RequestBody UserDTO userDTO){
        return Result.ok(loginService.register(userDTO));
    }
}
