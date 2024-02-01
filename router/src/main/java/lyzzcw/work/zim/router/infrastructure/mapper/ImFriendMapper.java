package lyzzcw.work.zim.router.infrastructure.mapper;

import lyzzcw.work.zim.router.infrastructure.entity.ImFriend;

/**
* Created by Mybatis Generator on 2024/02/01
*/
public interface ImFriendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImFriend record);

    int insertSelective(ImFriend record);

    ImFriend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImFriend record);

    int updateByPrimaryKey(ImFriend record);
}