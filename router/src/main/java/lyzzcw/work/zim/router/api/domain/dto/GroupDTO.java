package lyzzcw.work.zim.router.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 9:52
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    /**
     * 编号
     */
    private Long id;
    /**
     * 群主
     */
    private Long creator;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 受邀者
     */
    private List<Long> invitees;
}
