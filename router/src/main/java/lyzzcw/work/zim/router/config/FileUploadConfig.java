package lyzzcw.work.zim.router.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/10/21 10:30
 * Description: No Description
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "upload", ignoreInvalidFields = true)
@RefreshScope
public class FileUploadConfig {
    private String bucket;
    private String groupFile;
    private String groupImage;
    private String privateFile;
    private String privateImage;
}
