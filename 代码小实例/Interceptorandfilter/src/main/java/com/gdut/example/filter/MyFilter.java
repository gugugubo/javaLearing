package com.gdut.example.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


@Component
public class MyFilter implements Filter {
    //该方法在容器启动初始化过滤器时被调用，它在 Filter 的整个生命周期只会被调用一次。
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter 在容器启动初始化时被调用了");
    }

    //容器中的每一次请求都会调用该方法， FilterChain 用来调用下一个过滤器 Filter
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 响应对容器的每一次请求");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    //当容器销毁 过滤器实例时调用该方法，一般在方法中销毁或关闭资源，在过滤器 Filter 的整个生命周期也只会被调用一次
    public void destroy() {
        System.out.println("Filter 在容器销毁时被调用了");
    }
}
