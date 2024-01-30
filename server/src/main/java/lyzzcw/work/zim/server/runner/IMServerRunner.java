package lyzzcw.work.zim.server.runner;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.server.ws.IMNettyServer;
import lyzzcw.work.zim.registry.api.RegistryService;
import lyzzcw.work.zim.registry.api.config.RegistryConfig;
import lyzzcw.work.zim.registry.api.config.ServiceMeta;
import lyzzcw.work.zim.spi.loader.ExtensionLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;


/**
 * IMServer 运行程序
 *
 * @author lzy
 * @date 2023/12/20
 */
@Component
@Slf4j
public class IMServerRunner implements CommandLineRunner {

    @Resource
    private IMNettyServer imNettyServer;
    @Resource
    private ServiceMeta serviceMeta;

    /**
     * 判断服务是否准备完毕
     */
    public boolean isReady() {
        if (!imNettyServer.isReady()) {
            return false;
        }
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        //启动服务
        imNettyServer.start();
        //注册服务
        if(isReady()){
            log.info("registry info :{}",serviceMeta);
            RegistryConfig registryConfig = new RegistryConfig(serviceMeta.getServiceAddr(),
                    "nacos","random");
            RegistryService registryService = ExtensionLoader.getExtension(RegistryService.class,
                    registryConfig.getRegistryType());

            registryService.init(registryConfig);
            registryService.register(serviceMeta);
        }
        //管理mq
    }

    @PreDestroy
    public void destroy() {
        imNettyServer.shutdown();
    }
}
