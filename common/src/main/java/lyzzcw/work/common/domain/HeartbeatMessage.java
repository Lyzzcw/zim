package lyzzcw.work.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0.0
 * @description 心跳信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatMessage extends AbstractMessage{
    /**
     * 用户 ID
     */
    private Long userId;

    private HeartbeatMessage(Builder builder) {
        setCmd(builder.cmd);
        setData(builder.data);
        setSendResult(builder.sendResult);
        setUserId(builder.userId);
    }


    public static final class Builder {
        private Integer cmd;
        private Object data;
        private Boolean sendResult;
        private Long userId;

        public Builder() {
        }

        public Builder cmd(Integer val) {
            cmd = val;
            return this;
        }

        public Builder data(Object val) {
            data = val;
            return this;
        }

        public Builder sendResult(Boolean val) {
            sendResult = val;
            return this;
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public HeartbeatMessage build() {
            return new HeartbeatMessage(this);
        }
    }
}
