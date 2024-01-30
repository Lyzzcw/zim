package lyzzcw.work.zim.registry.nacos;


import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.loadbalance.api.ServiceLoadBalancer;
import lyzzcw.work.zim.registry.api.RegistryService;
import lyzzcw.work.zim.registry.api.config.RegistryConfig;
import lyzzcw.work.zim.registry.api.config.ServiceMeta;
import lyzzcw.work.zim.registry.nacos.helper.NacosUrlHelper;
import lyzzcw.work.zim.spi.annotation.SPIClass;
import lyzzcw.work.zim.spi.loader.ExtensionLoader;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于Nacos的注册服务
 */
@SPIClass
@Slf4j
public class NacosRegistryService implements RegistryService {


    private NamingService namingService;
    //负载均衡接口
    private ServiceLoadBalancer<ServiceMeta> serviceLoadBalancer;

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {
        namingService = NamingFactory.createNamingService(
                NacosUrlHelper.url2Properties(registryConfig.getRegistryAddr()));
        this.serviceLoadBalancer = ExtensionLoader.getExtension(
                ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
        if (log.isDebugEnabled()) {
            log.debug("nacos registry loaded successfully");
        }
    }

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        Instance instance = this.transferInstance(serviceMeta);
        namingService.registerInstance(instance.getServiceName(), instance);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        Instance instance = this.transferInstance(serviceMeta);
        namingService.deregisterInstance(instance.getServiceName(), instance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode, String sourceIp) throws Exception {
        serviceName = serviceName.replace("#","_");
        final List<Instance> allInstances = namingService.getAllInstances(serviceName);
        if (CollectionUtils.isEmpty(allInstances)) {
            log.warn("no instances found for service :{}" ,serviceName);
            return null;
        }

        final List<ServiceMeta> serviceMetaList = allInstances.stream()
                .map(item -> {
                    final ServiceMeta serviceMeta = this.mapToMeta(item.getMetadata());
                    return serviceMeta;
                })
                .collect(Collectors.toList());

        return serviceLoadBalancer.select(serviceMetaList, invokerHashCode, sourceIp);
    }

    @Override
    public void destroy() throws Exception {
        namingService.shutDown();
    }

    //nacos的服务名只支持 0-9a-zA-Z-,_: 因此需要把服务名# 替换
    private Instance transferInstance(ServiceMeta serviceMeta) {
        final String serverKey = NacosUrlHelper.buildNacosServiceKey(serviceMeta.getServiceName(),
                serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup());
        final Instance instance = new Instance();
        instance.setServiceName(serverKey);
        instance.setIp(serviceMeta.getServiceAddr());
        instance.setPort(serviceMeta.getServicePort());
        instance.setWeight(serviceMeta.getWeight());
        instance.setMetadata(this.metaToMap(serviceMeta));

        final Service service = new Service();
        service.setName(serverKey);
        service.setGroupName(serviceMeta.getServiceGroup());

        return instance;
    }

    private Map<String, String> metaToMap(ServiceMeta serviceMeta) {
        try {
            Map<String, String> map = BeanUtils.describe(serviceMeta);
            //        map.remove("class"); // 移除多余的 "class" 属性
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ServiceMeta mapToMeta(Map<String, String> map) {
        ServiceMeta serviceMeta = null;
        try {
            serviceMeta = new ServiceMeta();
            BeanUtils.populate(serviceMeta, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceMeta;
    }

}
