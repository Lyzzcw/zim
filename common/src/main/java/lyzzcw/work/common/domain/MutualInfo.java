package lyzzcw.work.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/31 9:27
 * Description: 客户端交互实例
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MutualInfo<T> {
    /**
     * 命令类型 IMCmdType枚举的值
     */
    private Integer cmd;

    T info;

    private MutualInfo(Builder<T> builder) {
        setCmd(builder.cmd);
        setInfo(builder.info);
    }


    public static final class Builder<T> {
        private Integer cmd;
        private T info;

        public Builder() {
        }

        public Builder<T> cmd(Integer val) {
            cmd = val;
            return this;
        }

        public Builder<T> info(T val) {
            info = val;
            return this;
        }

        public MutualInfo<T> build() {
            return new MutualInfo<T>(this);
        }
    }
}
