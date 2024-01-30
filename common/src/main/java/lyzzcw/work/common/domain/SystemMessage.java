package lyzzcw.work.common.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0.0
 * @description 系统消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage<T> extends AbstractMessage<T>{

    /**
     * 接收消息的用户id
     */
    private Long receiveId;

}
