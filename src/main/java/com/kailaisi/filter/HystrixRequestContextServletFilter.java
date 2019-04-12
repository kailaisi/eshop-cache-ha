package com.kailaisi.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import java.io.IOException;

/**
 * 描述：过滤器，过滤请求上下问
 * <p/>作者：wu
 * <br/>创建时间：2019/4/11 16:08
 */
public class HystrixRequestContextServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HystrixRequestContext requestContext = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } finally {
            requestContext.shutdown();
        }
    }

    @Override
    public void destroy() {

    }
}
