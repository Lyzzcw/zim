package lyzzcw.work.zim.router.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * create by lzy
 * im_group
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImGroup {
    /**
     * id
     */
    private Long id;

    /**
     * 群名字
     */
    private String name;

    /**
     * 群主id
     */
    private Long ownerId;

    /**
     * 群头像
     */
    private String headImage;

    /**
     * 群头像缩略图
     */
    private String headImageThumb;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 群备注
     */
    private String remark;

    /**
     * 是否已删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}