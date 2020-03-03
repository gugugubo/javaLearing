package com.gcb.learning.mybatisplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcb.learning.mybatisplus.ums.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author test
 * @since 2019-12-16
 */
@Mapper
@Component
public interface AdminMapper extends BaseMapper<Admin> {


}
