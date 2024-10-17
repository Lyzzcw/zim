package lyzzcw.work.zim.router.service.impl;

import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.infrastructure.mapper.ImMessageMapper;
import lyzzcw.work.zim.router.service.ImMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/1 9:32
 * Description: No Description
 */
@Service
public class ImMessageServiceImpl implements ImMessageService {
    @Resource
    private ImMessageMapper imMessageMapper;

    @Override
    public Integer persistent(ImMessage imMessage) {
        return imMessageMapper.insert(imMessage);
    }

    @Override
    public void updateMessageByCode(ImMessage imMessage) {
        imMessageMapper.updateMessageByCode(imMessage);
    }

}
