package com.gcb.learning.mybatisplus.ums.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gcb.learning.mybatisplus.ums.entity.Admin;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author test
 * @since 2019-12-16
 */
public interface AdminService extends IService<Admin> {

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     */
    List<Permission> getPermissionList(Long adminId);


    UserDetails loadUserByUsername(String username);

    Admin getAdminByUsername(String username);

    Admin register(Admin umsAdminParam);

    String login(String username, String password);
}
