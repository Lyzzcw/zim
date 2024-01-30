package lyzzcw.work.common.rocketmq.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 14:17
 * Description: No Description
 */
public class ProducerManager {

    private volatile static ProducerManager producerManager = null;

    private ProducerManager(){}

    public static ProducerManager getInstance(){
        if(null == producerManager){
            synchronized (ProducerManager.class){
                if(null == producerManager){
                    producerManager = new ProducerManager();
                }
            }
        }
        return producerManager;
    }

    private static Map<String,MessageQueueProducer> maps = new ConcurrentHashMap<>();

    public Map<String,MessageQueueProducer> getProducers(){
        return maps;
    }

    public void addProducer(String id, MessageQueueProducer producer){
        maps.put(id, producer);
    }

    public void remove(String id){
        maps.remove(id);
    }

    public MessageQueueProducer getServerProducer(String id){
        return maps.get(id);
    }

    public boolean contains(String id){
        return maps.containsKey(id);
    }

    public void refresh(List<String> ids){
        Iterator<Map.Entry<String, MessageQueueProducer>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MessageQueueProducer> entry = iterator.next();
            if(!ids.contains(entry.getKey())){
                //移除
                entry.getValue().shutdown();
                this.remove(entry.getKey());
            }
        }
    }
}
