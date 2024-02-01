package lyzzcw.work.zim.router.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * create by lzy
 * im_friend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImFriend {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 好友昵称
     */
    private String friendNickName;

    /**
     * 好友头像
     */
    private String friendHeadImage;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}