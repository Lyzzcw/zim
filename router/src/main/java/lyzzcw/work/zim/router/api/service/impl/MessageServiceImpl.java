package lyzzcw.work.zim.router.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.zim.router.api.domain.dto.MessageDTO;
import lyzzcw.work.zim.router.api.service.MessageService;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;
import lyzzcw.work.zim.router.infrastructure.mapper.ImMessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lzy
 * @version 1.0
 * Date: 2024/2/6 15:04
 * Description: No Description
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private ImMessageMapper imMessageMapper;

    @Override
    public PageInfo<ImMessage> page(MessageDTO messageDTO) {
        PageHelper.startPage(messageDTO.getPageNum(), messageDTO.getPageSize());
        return new PageInfo<>(imMessageMapper.page(messageDTO));
    }
}
