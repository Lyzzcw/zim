package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author lzy
 * @version 1.0.0
 * @description 私聊消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessage extends AbstractMessage{

    /**
     * 发消息的用户
     */
    private Long senderId;

    /**
     * 接收消息的用户id
     */
    private Long receiveId;

}
