package lyzzcw.work.zim.router.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 9:09
 * Description: No Description
 */
public class LoadBalancerUtil extends Balancer {
    public LoadBalancerUtil(){}

    public static Instance getWSInstance(List<Instance> instances) {
        return Balancer.getHostByRandomWeight(instances);
    }
}
