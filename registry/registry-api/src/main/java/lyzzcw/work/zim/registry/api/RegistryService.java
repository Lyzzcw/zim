package lyzzcw.work.zim.registry.api;


import lyzzcw.work.zim.registry.api.config.RegistryConfig;
import lyzzcw.work.zim.registry.api.config.ServiceMeta;
import lyzzcw.work.zim.spi.annotation.SPI;

/**
 * @author lzy
 * @version 1.0.0
 * @description
 */
@SPI("nacos")
public interface RegistryService {

    /**
     * 默认初始化方法
     */
    default void init(RegistryConfig registryConfig) throws Exception {

    }

    /** 服务注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务取消注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @param invokerHashCode HashCode值
     * @param sourceIp 源IP地址
     * @return 服务元数据
     * @throws Exception 抛出异常
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode,String sourceIp) throws Exception;

    /**
     * 服务销毁
     * @throws Exception 抛出异常
     */
    void destroy() throws Exception;
}
