package lyzzcw.work.zim.router;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:27
 * Description: No Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"lyzzcw.work.zim.router.infrastructure.mapper"})
public class RouterStarter {
    public static void main(String[] args) {
        SpringApplication.run(RouterStarter.class, args);
    }
}