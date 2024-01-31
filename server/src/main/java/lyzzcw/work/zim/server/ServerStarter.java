package lyzzcw.work.zim.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/29 9:30
 * Description: No Description
 */
@SpringBootApplication
public class ServerStarter {
    public static void main(String[] args) {
//        SpringApplication.run(ServerStarter.class, args);
        //去掉tomcat，无依赖启动
        new SpringApplicationBuilder(ServerStarter.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
