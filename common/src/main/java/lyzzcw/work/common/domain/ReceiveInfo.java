package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 接收到的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveInfo{

    /**
     * 命令类型 IMCmdType枚举的值
     */
    private Integer cmd;
    /**
     * 发送消息的用户
     */
    private Long senderId;
    /**
     * 组 ID
     */
    private Long groupId;
    /**
     *  是否需要回调发送结果
     */
    private Boolean sendResult = Boolean.FALSE;
    /**
     * 推送消息体
     */
    private Object data;

}
