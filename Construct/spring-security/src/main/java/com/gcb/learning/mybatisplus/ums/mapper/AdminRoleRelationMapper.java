package com.gcb.learning.mybatisplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcb.learning.mybatisplus.ums.entity.AdminRoleRelation;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author test
 * @since 2020-01-24
 */
@Mapper
@Component
public interface AdminRoleRelationMapper extends BaseMapper<AdminRoleRelation> {

    List<Permission> getPermissionList(Long adminId);
}
