package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0.0
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendResult<T> {

    /**
     * 发送消息的用户
     */
    private Long senderId;

    /**
     * 接收消息的用户
     */
    private Long receiverId;

    /**
     * 发送状态 IMCmdType
     */
    private Integer code;

    /**
     *  消息内容
     */
    private T data;

}
