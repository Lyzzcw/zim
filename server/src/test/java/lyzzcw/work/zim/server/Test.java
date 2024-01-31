package lyzzcw.work.zim.server;

import lyzzcw.work.common.domain.HeartbeatMessage;
import lyzzcw.work.common.domain.LoginMessage;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/1/31 11:33
 * Description: No Description
 */
public class Test {
    @org.junit.Test
    public void heartbeatMessage(){
        HeartbeatMessage heartbeatMessage = new HeartbeatMessage.Builder()
                .cmd(IMCmdType.HEART_BEAT.code())
                .sendResult(false)
                .data("ping")
                .build();
        MutualInfo<HeartbeatMessage> heartMessage = new MutualInfo.Builder<HeartbeatMessage>()
                .cmd(IMCmdType.HEART_BEAT.code())
                .info(heartbeatMessage)
                .build();
        System.out.println(JacksonUtil.to(heartMessage));
    }

    @org.junit.Test
    public void print(){
        LoginMessage loginMessage = new LoginMessage.Builder()
                .cmd(IMCmdType.LOGIN.code())
                .sendResult(false)
                .data("login")
                .userId(123L)
                .build();
        MutualInfo<LoginMessage> mutualInfo = new MutualInfo.Builder<LoginMessage>()
                .cmd(IMCmdType.LOGIN.code())
                .info(loginMessage)
                .build();
        System.out.println(JacksonUtil.to(mutualInfo));
    }
}
