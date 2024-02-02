package lyzzcw.work.zim.router.loadbalance;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.google.api.client.util.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.ProducerManager;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueProducerImpl;
import lyzzcw.work.zim.router.rocketmq.RocketMQUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * NACOS 服务 服务
 *
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 16:30
 * Description: No Description
 * @date 2024/02/02
 */
@Component
@Slf4j
public class NacosServiceService implements CommandLineRunner {

    private static final String SERVER_NAME = "zim-server";

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String namespace;
    @Value("${rocketmq.namesvr}")
    private String namesvr;

    private NamingService namingService;


    /**
     * 发现
     *
     * @return {@link String}
     * @throws NacosException Nacos 异常
     */
    public String discovery() throws NacosException {
        Instance instance = LoadBalancerUtil.getWSInstance(
                this.namingService.getAllInstances(SERVER_NAME));
        Assert.notNull(instance, "not found instance for zim-server");
        return instance.getIp();
    }

    /**
     * 监听服务注册信息
     *
     * @throws NacosException Nacos 异常
     */
    public void listener() throws NacosException {
        // 创建Nacos服务实例
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("namespace", namespace);
        this.namingService = NamingFactory.createNamingService(properties);

        // 注册监听器，实现监听逻辑
        this.namingService.subscribe(SERVER_NAME, new EventListener() {
            @SneakyThrows
            @Override
            public void onEvent(Event event) {
                if (event instanceof NamingEvent) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    log.info("zim-server instance changed:{}", instances);
                    List<String> serverIds = Lists.newArrayList();
                    // 处理服务实例列表变化的逻辑
                    for (Instance instance : instances) {
                        String serverId = instance.getMetadata().get("id");
                        serverIds.add(serverId);
                        if(!ProducerManager.getInstance().contains(serverId)){
                            ProducerManager.getInstance().addProducer(serverId,
                                    RocketMQUtil.messageSendProducer(namesvr,serverId));
                        }
                    }
                    //关闭废弃producer
                    ProducerManager.getInstance().refresh(serverIds);
                    log.info("zim-server producers:{}",ProducerManager.getInstance().getProducers());
                }
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        this.listener();
    }
}
