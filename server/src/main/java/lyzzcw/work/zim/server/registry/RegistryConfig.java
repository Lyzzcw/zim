package lyzzcw.work.zim.server.registry;

import lyzzcw.work.zim.registry.api.config.ServiceMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 8:52
 * Description: No Description
 */
@Configuration
public class RegistryConfig {

    @Value("${server.registryAddress}")
    private String registryAddress;
    @Value("${spring.application.name}")
    private String name;
    @Value("${server.id}")
    private String id;
    @Value("${server.version}")
    private String version;
    @Value("${server.group}")
    private String group;
    @Value("${server.weight}")
    private Integer weight;
    @Value("${websocket.port}")
    private Integer port;

    @Bean
    public ServiceMeta initRegistryInfo(){
        return new ServiceMeta(id,this.name,this.version,this.registryAddress,this.port,this.group,this.weight);
    }
}
