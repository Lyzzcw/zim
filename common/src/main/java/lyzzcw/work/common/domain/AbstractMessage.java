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
public abstract class AbstractMessage {

    /**
     * 命令类型 IMCmdType枚举的值
     */
    private Integer cmd;

    /**
     * 消息码
     */
    private Long messageCode;

    /**
     * 推送消息的数据
     */
    private Object data;

    /**
     * 是否需要回推发送结果,默认true
     */
    private Boolean sendResult = Boolean.TRUE;


}
