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
@WebFilter(urlPatterns = "/viewlog/*", filterName = "viewFilter")
public class ViewFilter implements Filter {
    private static final String USER_AGENT = "USER-AGENT";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("---过滤器收到访问请求：getRequestURI={},getRemoteHost={},getRemotePort={},{}={}",
                request.getRequestURI(),
                request.getRemoteHost(),
                request.getRemotePort(),
                USER_AGENT,
                request.getHeader(USER_AGENT));
        String path = request.getParameter("path");
/*        if (path != null && path.length() > 0) {
            log.info("请求文件目录：{}", path);
            if (!LogUtil.isLegal(path)) {
                log.error("请求不合法：{}", path);
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write("请求路径非法");
                return;
            }
        }*/

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
