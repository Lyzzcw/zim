package lyzzcw.work.zim.loadbalance.random;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.loadbalance.api.ServiceLoadBalancer;
import lyzzcw.work.zim.spi.annotation.SPIClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于随机算法的负载均衡策略
 */
@Slf4j
@SPIClass
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    @Override
    public T select(List<T> servers, int hashCode,String sourceIp) {
        if(log.isDebugEnabled()){
            log.debug("Load balancing policy based on random");
        }
        if(CollectionUtils.isEmpty(servers)){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
