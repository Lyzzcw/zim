package lyzzcw.work.zim.router;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.file.FileUtil;
import lyzzcw.work.component.common.utils.EncryptUtil;
import lyzzcw.work.component.minio.oss.config.MinioConfig;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import lyzzcw.work.zim.router.util.SpringContextHolder;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/4 15:13
 * Description: No Description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RouterStarter.class,
        properties = {"spring.profiles.active=dev"}
)
public class RouterTest {
    @Resource
    private MinioTemplate minioTemplate;
    @Test
    public void upload() throws FileNotFoundException {
        String base64 = FileUtil.image_to_base64("C:/Users/84428/Desktop/icon.jpg");
        byte[] decodedBytes = EncryptUtil.base64_decode(base64);
        try(InputStream inputStream = new ByteArrayInputStream(decodedBytes);
            InputStream uploadStream = new ByteArrayInputStream(decodedBytes)){
            ImageInfo imageInfo = Imaging.getImageInfo(inputStream, null);
            // 获取图片格式信息
            ImageFormat imageFormat = imageInfo.getFormat();
            String url = minioTemplate.upload("zim",
                    "/private/"+System.currentTimeMillis()+"."+imageFormat.getName(),uploadStream);
            System.out.println(url);
        }catch (IOException | ImageReadException e) {
            e.printStackTrace();
        }
    }
}
