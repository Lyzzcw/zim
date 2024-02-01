package lyzzcw.work.zim.router.processor;


import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;

/**
 * 消息处理器
 *
 * @author lzy
 * @date 2023/12/20
 */
public interface MessageProcessor<T> {

    /**
     * 处理数据
     */
    default void process(T info){

    }

    default <T> T transform(Object obj){
        return (T)obj;
    }

     ImMessage persistenceData(T t);
}
