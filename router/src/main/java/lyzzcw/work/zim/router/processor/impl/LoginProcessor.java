package lyzzcw.work.zim.router.processor.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.LoginMessage;
import lyzzcw.work.common.domain.PrivateMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.common.enums.MessageStatus;
import lyzzcw.work.common.enums.MessageType;
import lyzzcw.work.common.rocketmq.service.MessageQueueProducer;
import lyzzcw.work.component.common.id.SnowflakeIdWorker;
import lyzzcw.work.component.common.json.jackson.JacksonUtil;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.rocketmq.RocketMQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class LoginProcessor implements MessageProcessor<LoginMessage> {

    @Autowired
    @Qualifier("persistenceProducer")
    private MessageQueueProducer persistenceProducer;

    @Override
    public void process(LoginMessage data) {
        log.info("received login message:{}",data);
        //生成唯一消息码
        Long messageCode = SnowflakeIdWorker.generateId();
        data.setMessageCode(messageCode);
        //放入持久化队列
        persistenceProducer.sendMessage(RocketMQUtil.buildPersistenceMessage(
                JacksonUtil.to(this.persistenceData(data))));
    }

    @Override
    public ImMessage persistenceData(LoginMessage loginMessage) {
        ImMessage imMessage = new ImMessage();
        imMessage.setContent(loginMessage.getData().toString());
        imMessage.setMessageType(MessageType.TEXT.code());
        imMessage.setRecvId(loginMessage.getUserId());
        imMessage.setSendId(loginMessage.getUserId());
        imMessage.setStatus(MessageStatus.SENDED.code());
        imMessage.setSendTime(LocalDateTime.now());
        imMessage.setCmdType(IMCmdType.LOGIN.code());
        imMessage.setMessageCode(loginMessage.getMessageCode());
        return imMessage;
    }

    @Override
    public LoginMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new LoginMessage(), false);
    }
}
