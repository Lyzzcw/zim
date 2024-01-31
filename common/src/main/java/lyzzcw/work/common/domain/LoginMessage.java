package lyzzcw.work.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

/**
 * @author lzy
 * @version 1.0.0
 * @description 登录信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginMessage extends AbstractMessage{

    /**
     * 用户 ID
     */
    private Long userId;

    private LoginMessage(Builder builder) {
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

        public LoginMessage build() {
            return new LoginMessage(this);
        }
    }
}
