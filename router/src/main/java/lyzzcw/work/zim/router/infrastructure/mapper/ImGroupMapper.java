package lyzzcw.work.zim.router.infrastructure.mapper;

import lyzzcw.work.zim.router.infrastructure.entity.ImGroup;

/**
* Created by Mybatis Generator on 2024/02/01
*/
public interface ImGroupMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(ImGroup record);

    int insertSelective(ImGroup record);

    ImGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImGroup record);

    int updateByPrimaryKey(ImGroup record);
}