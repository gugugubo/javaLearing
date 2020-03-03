package com.gcb.learning.mybatisplus.security.entity;

import com.gcb.learning.mybatisplus.ums.entity.Admin;
import com.gcb.learning.mybatisplus.ums.entity.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminUserDetails implements UserDetails , Serializable {
    private Admin admin;
    private List<Permission> permissionList;

    public AdminUserDetails(Admin admin, List<Permission> permissionList) {
        this.admin = admin;
        this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission.getValue() != null) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getValue());
                list.add(simpleGrantedAuthority);
            }
        }
        return list;
    }


    @Override
    public String getPassword() {
        return admin.getPassword();
    }


    @Override
    public String getUsername() {
        return admin.getUsername();
    }


    @Override
    /**
     *账号是否未过期，默认是false
     */
    public boolean isAccountNonExpired() {
        return true;
    }



    /**
     *账号是否未锁定，默认是false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *  账号凭证是否未过期，默认是false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return admin.getStatus().equals(1);
    }
}
