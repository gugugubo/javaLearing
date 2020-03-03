package com.gcb.learning.mybatisplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 后台用户权限表 Mapper 接口
 * </p>
 *
 * @author test
 * @since 2020-01-24
 */
@Mapper
@Component
public interface PermissionMapper extends BaseMapper<Permission> {

}
