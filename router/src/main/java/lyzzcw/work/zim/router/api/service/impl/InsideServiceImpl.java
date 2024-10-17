package lyzzcw.work.zim.router.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.MutualInfo;
import lyzzcw.work.common.domain.InsideMessage;
import lyzzcw.work.common.enums.IMCmdType;
import lyzzcw.work.zim.router.api.service.InsideService;
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
public class InsideServiceImpl implements InsideService {


    @Override
    public Boolean send(Long userId, String message) {
        //消息下发队列
        InsideMessage insideMessage = new InsideMessage();
        insideMessage.setReceiveId(userId);
        insideMessage.setSendResult(false);
        insideMessage.setCmd(IMCmdType.INSIDE_MESSAGE.code());
        insideMessage.setData(message);
        MutualInfo<InsideMessage> mutualInfo = new MutualInfo.Builder<InsideMessage>()
                .cmd(insideMessage.getCmd())
                .info(insideMessage)
                .build();
        MessageProcessor<?> processor = ProcessorFactory.getProcessor(IMCmdType.fromCode(mutualInfo.getCmd()));
        processor.process(processor.transform(insideMessage));
        return Boolean.TRUE;
    }
}
