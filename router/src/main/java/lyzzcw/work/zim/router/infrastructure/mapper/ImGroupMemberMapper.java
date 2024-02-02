package lyzzcw.work.zim.router.infrastructure.mapper;

import lyzzcw.work.zim.router.infrastructure.entity.ImGroupMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2024/02/01
*/
public interface ImGroupMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImGroupMember record);

    int insertSelective(ImGroupMember record);

    ImGroupMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImGroupMember record);

    int updateByPrimaryKey(ImGroupMember record);

    List<ImGroupMember> findGroupMembersByGroupId(@Param("groupId") Long groupId);
}