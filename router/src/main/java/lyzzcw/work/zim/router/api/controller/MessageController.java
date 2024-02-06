package lyzzcw.work.zim.router.api.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.domain.dto.MessageDTO;
import lyzzcw.work.zim.router.api.service.MessageService;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 15:03
 * Description: No Description
 */
@RequestMapping("message")
@RestController
@Slf4j
@Api("聊天记录管理")
public class MessageController {
    @Resource
    private MessageService messageService;

    @PostMapping("page")
    @ApiOperation("聊天历史")
    public Result<PageInfo<ImMessage>> page(@RequestBody MessageDTO messageDTO){
        return Result.ok(messageService.page(messageDTO));
    }
}
