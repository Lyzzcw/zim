package lyzzcw.work.zim.router.loadbalance;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.api.client.util.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.rocketmq.domain.MQConstants;
import lyzzcw.work.common.rocketmq.domain.MessageQueueConfig;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.common.rocketmq.service.ProducerManager;
import lyzzcw.work.common.rocketmq.service.impl.MessageQueueProducerImpl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 16:30
 * Description: No Description
 */
@Component
@Slf4j
public class NacosServiceListener implements CommandLineRunner {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String namespace;
    @Value("${rocketmq.namesvr}")
    private String namesvr;

    public void start() throws NacosException {
        // 创建Nacos服务实例
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("namespace", namespace);
        NamingService namingService = NamingFactory.createNamingService(properties);

        // 注册监听器，实现监听逻辑
        namingService.subscribe("zim-server", new EventListener() {
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
                            ProducerManager.getInstance().addProducer(serverId,messageSendProducer(serverId));
                        }
                    }
                    //关闭废弃producer
                    ProducerManager.getInstance().refresh(serverIds);
                    log.info("zim-server producers:{}",ProducerManager.getInstance().getProducers());
                }
            }
        });
    }


    private MessageQueueProducer messageSendProducer(String id) throws MQClientException {
        MessageQueueConfig config = new MessageQueueConfig();
        config.setNamesvr(namesvr);
        config.setTopic(MQConstants.MESSAGE_FROM_ROUTE_TOPIC.concat(id));
        config.setGroup(MQConstants.MESSAGE_FROM_ROUTE_GROUP.concat(id));
        MessageQueueProducer producer = new MessageQueueProducerImpl();
        producer.initProducer(config);
        return producer;
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
}
