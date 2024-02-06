package lyzzcw.work.zim.router.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 15:06
 * Description: No Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "聊天记录请求")
public class MessageDTO{
    @ApiModelProperty(value = "页数")
    private int pageNum = 1;
    @ApiModelProperty(value = "页码")
    private int pageSize = 10;
    @ApiModelProperty(value = "群组id")
    private Long groupId;
    @ApiModelProperty(value = "发送者")
    private Long sendId;
    @ApiModelProperty(value = "接收者")
    private Long recvId;
    @ApiModelProperty(value = "消息类型")
    private Integer cmdType;
    @ApiModelProperty(value = "信息类型")
    private Integer messageType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "起始时间")
    private LocalDateTime beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
}
