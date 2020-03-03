package com.gcb.learning.mybatisplus.ums.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcb.learning.mybatisplus.security.config.SecurityConfig;
import com.gcb.learning.mybatisplus.security.entity.AdminUserDetails;
import com.gcb.learning.mybatisplus.security.utils.JwtTokenUtil;
import com.gcb.learning.mybatisplus.ums.entity.Admin;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import com.gcb.learning.mybatisplus.ums.mapper.AdminMapper;
import com.gcb.learning.mybatisplus.ums.mapper.AdminRoleRelationMapper;
import com.gcb.learning.mybatisplus.ums.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author test
 * @since 2019-12-16
 */

@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService, UserDetailsService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    AdminRoleRelationMapper adminRoleRelationMapper;

    //@Autowired
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    //@Autowired
    private PasswordEncoder passwordEncoder = new SecurityConfig().passwordEncoder();

    @Override
    public List<Permission> getPermissionList(Long adminId) {
        return adminRoleRelationMapper.getPermissionList(adminId);
    }


    @Override
    public Admin getAdminByUsername(String username) {
        Map<String,Object> columnMap = new HashMap<>();
        //写表中的列名
        columnMap.put("username",username);
        List<Admin> employees = adminMapper.selectByMap(columnMap);
        if (employees!=null && employees.size()>0){
            return  employees.get(0);
        }
        else {
            return null;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username){
        Admin admin = getAdminByUsername(username);
        if (admin!=null){
            List<Permission> permissionList =getPermissionList(admin.getId());
            return new AdminUserDetails(admin,permissionList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public Admin register(Admin umsAdminParam) {
        Admin umsAdmin = new Admin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        Map<String,Object> columnMap = new HashMap<>();
        //写表中的列名
        columnMap.put("username",umsAdmin.getUsername());
        List<Admin> employees = adminMapper.selectByMap(columnMap);
        if (employees!=null && employees.size()>0 ) {
            return null;
        }else {
            //将密码进行加密操作
            String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
            umsAdmin.setPassword(encodePassword);
            adminMapper.insert(umsAdmin);
            return umsAdmin;
        }
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            // 验证密码是否正确
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            // 进行登录操作
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }
}
