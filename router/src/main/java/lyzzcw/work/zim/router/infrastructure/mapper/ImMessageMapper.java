package lyzzcw.work.zim.router.infrastructure.mapper;

import lyzzcw.work.zim.router.api.domain.dto.MessageDTO;
import lyzzcw.work.zim.router.infrastructure.entity.ImMessage;

import java.util.List;

/**
* Created by Mybatis Generator on 2024/02/01
*/
public interface ImMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImMessage record);

    int insertSelective(ImMessage record);

    ImMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImMessage record);

    int updateByPrimaryKeyWithBLOBs(ImMessage record);

    int updateByPrimaryKey(ImMessage record);

    List<ImMessage> page(MessageDTO messageDTO);

    void updateMessageByCode(ImMessage record);
}