package lyzzcw.work.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0.0
 * @description 发送信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMessage<T> {

    /**
     * 命令类型 IMCmdType枚举的值
     */
    private Integer cmd;

    /**
     * 推送消息的数据
     */
    private T data;

    /**
     * 是否需要回推发送结果,默认true
     */
    private Boolean sendResult = Boolean.FALSE;


}
