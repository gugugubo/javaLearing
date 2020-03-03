package com.gcb.learning.mybatisplus.pms.config;

import com.gcb.learning.mybatisplus.security.config.SecurityConfig;
import com.gcb.learning.mybatisplus.ums.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * security模块相关配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class MySecurityConfig extends SecurityConfig {

    @Autowired
    AdminService adminService;

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                return adminService.loadUserByUsername(s);
            }
        };
        return userDetailsService;
    }
}
