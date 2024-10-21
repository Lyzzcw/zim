package lyzzcw.work.zim.router.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import lyzzcw.work.zim.router.config.FileUploadConfig;
import lyzzcw.work.zim.router.api.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/10/21 9:57
 * Description: No Description
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private MinioTemplate minioTemplate;
    @Resource
    private FileUploadConfig fileUploadConfig;

    @Override
    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()){
            return minioTemplate.upload(fileUploadConfig.getBucket(),
                    fileUploadConfig.getGroupFile()+file.getOriginalFilename(),
                    inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
