package lyzzcw.work.common.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 10:57
 * Description: No Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageQueueConfig {
    private String namesvr;
    private String group;
    private String topic;
    private String tag = "*";
    private MessageModel messageModel = MessageModel.CLUSTERING;
}
