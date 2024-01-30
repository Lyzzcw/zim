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
public class HeartbeatMessage<T> extends AbstractMessage<T>{
    /**
     * 用户 ID
     */
    private Long userId;

    private HeartbeatMessage(Builder<T> builder) {
        setCmd(builder.cmd);
        setData(builder.data);
        setSendResult(builder.sendResult);
        setUserId(builder.userId);
    }


    public static final class Builder<T> {
        private Integer cmd;
        private T data;
        private Boolean sendResult;
        private Long userId;

        public Builder() {
        }

        public Builder<T> cmd(Integer val) {
            cmd = val;
            return this;
        }

        public Builder<T> data(T val) {
            data = val;
            return this;
        }

        public Builder<T> sendResult(Boolean val) {
            sendResult = val;
            return this;
        }

        public Builder<T> userId(Long val) {
            userId = val;
            return this;
        }

        public HeartbeatMessage<T> build() {
            return new HeartbeatMessage<T>(this);
        }
    }
}
