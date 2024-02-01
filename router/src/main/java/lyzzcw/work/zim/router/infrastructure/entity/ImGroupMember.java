package lyzzcw.work.zim.router.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * create by lzy
 * im_group_member
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImGroupMember {
    /**
     * id
     */
    private Long id;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 组内显示名称
     */
    private String aliasName;

    /**
     * 用户头像
     */
    private String headImage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已退出
     */
    private Integer quit;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}