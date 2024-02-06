package lyzzcw.work.zim.router.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * create by lzy
 * im_message
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImMessage {
    /**
     * id
     */
    private Long id;

    /**
     * 消息码
     */
    private Long messageCode;

    /**
     * 消息类型
     */
    private Integer cmdType;

    /**
     * 发送用户id
     */
    private Long sendId;

    /**
     * 接收用户id(群消息则为群id)
     */
    private Long recvId;

    /**
     * 消息内容类型 0:文字 1:图片 2:文件 3:语音
     */
    private Integer messageType;

    /**
     * 状态 0:未读 1:已读 2:撤回
     */
    private Integer status;

    /**
     * 发送用户昵称
     */
    private String sendNickName;

    /**
     * 被@的用户id列表，逗号分隔
     */
    private String atUserIds;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;

    /**
     * 发送内容
     */
    private String content;
}