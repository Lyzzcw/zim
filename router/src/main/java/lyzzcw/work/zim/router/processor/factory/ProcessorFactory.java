package lyzzcw.work.zim.router.processor.factory;


import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.processor.impl.*;
import lyzzcw.work.zim.router.util.SpringContextHolder;


/**
 * 处理器工厂
 *
 * @author lzy
 * @date 2023/12/20
 */
public class ProcessorFactory {

    public static MessageProcessor getProcessor(IMCmdType cmd){
        switch (cmd){
            //登录
            case LOGIN:
                return SpringContextHolder.getApplicationContext().getBean(LoginProcessor.class);
            //单聊消息
            case PRIVATE_MESSAGE:
                return SpringContextHolder.getApplicationContext().getBean(PrivateMessageProcessor.class);
            //群聊消息
            case GROUP_MESSAGE:
                return SpringContextHolder.getApplicationContext().getBean(GroupMessageProcessor.class);
            //系统消息
            case SYSTEM_MESSAGE:
                return SpringContextHolder.getApplicationContext().getBean(SystemMessageProcessor.class);
            //站内信
            case INSIDE_MESSAGE:
                return SpringContextHolder.getApplicationContext().getBean(InsideMessageProcessor.class);
            default:
                return null;

        }
    }
}
