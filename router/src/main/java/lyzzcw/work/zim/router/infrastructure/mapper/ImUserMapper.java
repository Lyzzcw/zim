package lyzzcw.work.zim.router.infrastructure.mapper;

import lyzzcw.work.zim.router.infrastructure.entity.ImUser;

/**
* Created by Mybatis Generator on 2024/02/01
*/
public interface ImUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImUser record);

    int insertSelective(ImUser record);

    ImUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImUser record);

    int updateByPrimaryKey(ImUser record);
}