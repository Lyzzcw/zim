package lyzzcw.work.zim.router.api.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.domain.common.entity.Result;
import lyzzcw.work.zim.router.api.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/10/21 9:54
 * Description: No Description
 */
@RestController
@RequestMapping("file")
@Slf4j
@Api("文件管理")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload/file")
    public Result uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("文件不能为空");
        }
        return Result.ok(fileService.uploadFile(file));
    }

}
