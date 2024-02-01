package lyzzcw.work.zim.router.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * create by lzy
 * im_user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImUser {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 用户头像缩略图
     */
    private String headImageThumb;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别 0:男 1:女
     */
    private Integer sex;

    /**
     * 用户类型 1:普通用户 2:审核账户
     */
    private Short type;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}