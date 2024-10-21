package lyzzcw.work.zim.router.api.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/10/21 9:55
 * Description: No Description
 */
public interface FileService {
    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link String}
     */
    public String uploadFile(MultipartFile file);
}
