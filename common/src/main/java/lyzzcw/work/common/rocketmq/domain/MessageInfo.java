package lyzzcw.work.common.rocketmq.domain;

import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/30 11:08
 * Description: No Description
 */
public class MessageInfo extends Message {

    private MessageInfo(Builder builder) {
        setTopic(builder.topic);
        setFlag(builder.flag);
        setBody(builder.body.getBytes(StandardCharsets.UTF_8));
        setTransactionId(builder.transactionId);
    }

    public static final class Builder {
        private String topic;
        private int flag;
        private String body;
        private String transactionId;

        public Builder() {
        }

        public Builder topic(String val) {
            topic = val;
            return this;
        }

        public Builder flag(int val) {
            flag = val;
            return this;
        }

        public Builder body(String val) {
            body = val;
            return this;
        }

        public Builder transactionId(String val) {
            transactionId = val;
            return this;
        }

        public MessageInfo build() {
            return new MessageInfo(this);
        }
    }
}
