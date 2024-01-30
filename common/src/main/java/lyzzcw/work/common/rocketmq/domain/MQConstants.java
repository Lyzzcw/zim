package lyzzcw.work.common.rocketmq.domain;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 13:35
 * Description: No Description
 */
public interface MQConstants {

    /**
     * 消息到路由主题
     */
    String MESSAGE_TO_ROUTE_TOPIC = "MESSAGE_TO_ROUTE";
    /**
     * 向路由组发送消息
     */
    String MESSAGE_TO_ROUTE_GROUP = "MESSAGE_TO_ROUTE_GROUP";
    /**
     * 来自路由主题消息
     */
    String MESSAGE_FROM_ROUTE_TOPIC = "MESSAGE_FROM_ROUTE_";
    /**
     * 来自路由组消息
     */
    String MESSAGE_FROM_ROUTE_GROUP = "MESSAGE_FROM_ROUTE_GROUP_";

}
