package com.codeisrun.viewlog.filter;

import com.codeisrun.viewlog.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liubinqiang
 */
@Slf4j
@WebFilter(urlPatterns = {"/", "/viewlog/*"}, filterName = "viewFilter")
public class ViewFilter implements Filter {
    private static final String USER_AGENT = "USER-AGENT";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("---过滤器收到访问请求：\ngetRequestURI={}\ngetRemoteHost={}\ngetRemotePort={}\n{}={}",
                request.getRequestURI(),
                request.getRemoteHost(),
                request.getRemotePort(),
                USER_AGENT,
                request.getHeader(USER_AGENT));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
