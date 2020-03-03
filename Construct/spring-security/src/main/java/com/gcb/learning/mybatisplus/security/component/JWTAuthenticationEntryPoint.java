package com.gcb.learning.mybatisplus.security.component;

import cn.hutool.json.JSONUtil;
import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户没有访问权限时的处理器，用于返回JSON格式的处理结果；
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse, AuthenticationException authException)
            throws IOException{
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.getWriter().println(JSONUtil.parse(new CommonResult().unauthorized("JWTAuthenticationEntryPoint"+authException.getMessage())));
        // String reason = "统一处理，原因："+authException.getMessage();
        // httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(reason));
        httpServletResponse.getWriter().flush();
    }
}
