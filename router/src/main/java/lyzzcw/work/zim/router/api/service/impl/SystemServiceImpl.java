package lyzzcw.work.zim.router.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.SystemMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.router.api.service.SystemService;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import lyzzcw.work.zim.router.processor.factory.ProcessorFactory;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/2 16:00
 * Description: No Description
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {


    @Override
    public Boolean notice(Long userId, String message) {
        //消息下发队列
        SystemMessage systemMessage = new SystemMessage();
        systemMessage.setReceiveId(userId);
        systemMessage.setSendResult(false);
        systemMessage.setCmd(IMCmdType.SYSTEM_MESSAGE.code());
        systemMessage.setData(message);
        MutualInfo<SystemMessage> mutualInfo = new MutualInfo.Builder<SystemMessage>()
                .cmd(systemMessage.getCmd())
                .info(systemMessage)
                .build();
        MessageProcessor<?> processor = ProcessorFactory.getProcessor(IMCmdType.fromCode(mutualInfo.getCmd()));
        processor.process(processor.transform(mutualInfo.getInfo()));
        return Boolean.TRUE;
    }
}
