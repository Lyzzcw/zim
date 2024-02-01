package lyzzcw.work.zim.router.processor.impl;


import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.common.domain.GroupMessage;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.processor.MessageProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 群消息处理器
 *
 * @author lzy
 * @date 2023/12/21
 */
@Component
@Slf4j
public class GroupMessageProcessor implements MessageProcessor<GroupMessage> {


    @Async
    @Override
    public void process(GroupMessage data) {
        log.info("received group message:{}",data);
    }

    @Override
    public ImMessage persistenceData(GroupMessage groupMessage) {
        return null;
    }


    @Override
    public GroupMessage transform(Object obj) {
        Map<?, ?> map = (Map<?, ?>) obj;
        return BeanUtil.fillBeanWithMap(map, new GroupMessage(), false);
    }
}
