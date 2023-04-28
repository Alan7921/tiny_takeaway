package fun.xinliu.filter;


import com.alibaba.fastjson.JSON;
import fun.xinliu.common.BaseContext;
import fun.xinliu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheck implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("本次请求的URI为：{}",requestURI );

        // 列出无需拦截的urls
        String[] urls = new String[] {
                "/employee/login", "/employee/logout", "/backend/**", "/front/**", "/user/sendCode", "/user/login"
        };


        // 判断本次请求是否需要处理

        // 如果符合exclude的pattern的List 直接放行
        if(check(urls, requestURI)) {
            filterChain.doFilter(request,response);
            return;
        }

        // 如果不符合，则判断是否已经登陆, 如果已经登陆则放行
        if(request.getSession().getAttribute("employee") != null ) {

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("用户已登陆，用户id为: {}", empId);
            filterChain.doFilter(request,response);
            return;
        }

        // 如果不符合，则判断是否已经登陆, 如果已经登陆则放行
        if(request.getSession().getAttribute("user") != null ) {

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            log.info("用户已登陆，用户id为: {}", userId);
            filterChain.doFilter(request,response);
            return;
        }

        // 如果未登陆，则写数据返回前端
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }


    public boolean check(String[] urls, String requestURI) {
        for (String s:urls) {
            boolean match = PATH_MATCHER.match(s, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
