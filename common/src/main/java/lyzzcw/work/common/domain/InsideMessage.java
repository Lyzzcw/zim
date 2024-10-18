package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0.0
 * @description 站内信
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsideMessage extends AbstractMessage{

    /**
     * 接收消息的用户id
     */
    private Long receiveId;

    @Override
    public String toString() {
        return "InsideMessage{" +
                "receiveId=" + receiveId +
                "} " + super.toString();
    }
}
