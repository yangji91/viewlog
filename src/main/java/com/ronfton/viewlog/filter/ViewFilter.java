package com.ronfton.viewlog.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liubinqiang
 */

@WebFilter(urlPatterns = "/viewlog/*", filterName = "viewFilter")
public class ViewFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewFilter.class);
    private static final String USER_AGENT = "USER-AGENT";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOGGER.info("---过滤器收到访问请求：getRequestURI={},getRemoteHost={},getRemotePort={},{}={}",
                request.getRequestURI(),
                request.getRemoteHost(),
                request.getRemotePort(),
                USER_AGENT,
                request.getHeader(USER_AGENT));

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
