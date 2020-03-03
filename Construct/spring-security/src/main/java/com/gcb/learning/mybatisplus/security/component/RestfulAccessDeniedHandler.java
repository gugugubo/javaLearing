package com.gcb.learning.mybatisplus.security.component;

import cn.hutool.json.JSONUtil;
import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当未登录或token失效时，返回JSON格式的结果；
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().println(JSONUtil.parse(new CommonResult().forbidden("RestfulAccessDeniedHandler"+e.getMessage())));
        httpServletResponse.getWriter().flush();
    }
}
