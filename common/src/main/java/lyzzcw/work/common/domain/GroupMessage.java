package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lzy
 * @version 1.0.0
 * @description 群消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage extends AbstractMessage{

    /**
     * 组 ID
     */
    private Long groupId;
    /**
     * 发消息的用户
     */
    private Long senderId;
    /**
     * 接收群消息的用户
     */
    private Long receiveId;

    @Override
    public String toString() {
        return "GroupMessage{" +
                "groupId=" + groupId +
                ", senderId=" + senderId +
                ", receiveId=" + receiveId +
                "} " + super.toString();
    }
}
