package com.gcb.learning.mybatisplus.security.component;

import com.gcb.learning.mybatisplus.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    //@Qualifier("jwtUserDetailsService")选择注入指定的对象
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // @Value("${jwt.tokenHeader}")
    private String tokenHeader = "Authorization";
    // @Value("${jwt.tokenHead}")
    private String tokenHead = "Bearer";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)  throws ServletException, IOException{
        // 1.从header中获取凭证authToken
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            //去除token前带有的Bearer前缀的
            String authToken = authHeader.substring(this.tokenHead.length()+1);
            // 2.获取username
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            log.info("checking username:{}", username);
            // 3.看看上下文中是否有我们以这个username为标识的主体，这个上下文是一次请求有效的，用完就被clean了
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // 4.验证这个authToken 是否在有效期呢啊
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 5.生成主题信息
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 6.然后将主体信息————authentication，存入上下文环境
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
        //在视图页面返回给客户端之前执行，但是执行顺序在Interceptor之后
       // System.out.println("############TestFilter1 doFilter after############");
    }

}
