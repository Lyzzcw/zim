package lyzzcw.work.zim.router.api.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 14:36
 * Description: No Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户请求")
public class UserDTO {
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "性别")
    private Integer sex;
}
