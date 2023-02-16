package com.tommychan.takeout.filter;


import com.alibaba.fastjson.JSON;
import com.tommychan.takeout.common.BaseContext;
import com.tommychan.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

     //路径匹配器 支持通配符写法
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //拿到URI
        String requestURI = request.getRequestURI();
        log.info("请求为：{}", requestURI);
        //定义白名单
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //判断请求是否需要处理
        boolean check = check(requestURI, uris);
        //放行
        if (check) {
            log.info("放行请求：{}", requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //判断客户端 (管理端) 登录状态
        if (request.getSession().getAttribute("employee") != null) {
            Long id = (Long) request.getSession().getAttribute("employee");
            log.info("已登录，id为：{}，放行请求：{}", id, requestURI);
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request,response);
            return;
        }

        //判断移动端登录状态
        if (request.getSession().getAttribute("user") != null) {
            Long userid = (Long) request.getSession().getAttribute("user");
            log.info("已登录，id为：{}，放行请求：{}", userid, requestURI);
            BaseContext.setCurrentId(userid);
            filterChain.doFilter(request,response);
            return;
        }

        //未登录
        log.info("未登录，拦截到请求：{}", request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 检查本次请求是否需要放行
     * @param requestURI
     * @param uris
     * @return
     */
    public boolean check(String requestURI, String[] uris){
        for (String uri : uris){
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
