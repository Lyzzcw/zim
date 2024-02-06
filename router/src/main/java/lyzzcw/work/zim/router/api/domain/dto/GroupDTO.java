package lyzzcw.work.zim.router.api.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "群组请求")
public class GroupDTO {
    /**
     * 编号
     */
    @ApiModelProperty(value = "群组id")
    private Long id;
    /**
     * 群主
     */
    @ApiModelProperty(value = "群主")
    private Long creator;
    /**
     * 组名称
     */
    @ApiModelProperty(value = "群组名称")
    private String groupName;
    /**
     * 受邀者
     */
    @ApiModelProperty(value = "邀请人列表")
    private List<Long> invitees;
}
