package com.cse.naruto.context.interceptor;

import com.cse.naruto.model.Response;
import com.cse.naruto.model.User;
import com.cse.naruto.util.Constant;
import com.cse.naruto.util.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 请求拦截器
 *
 * @author 王振琦
 */
public class NarutoInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(NarutoInterceptor.class);

    private static final String LOGIN_PAGE_URI = "%s/login";
    private static final String USER_LOGIN_URI = "%s/api/users/login";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("NemoInterceptor: preHandle ----------");
        logger.info("User Address: " + request.getRemoteHost() + " ----------");
        logger.info("Request URL: " + request.getRequestURL().toString() + " ----------");
        logger.info("Request Method: " + request.getMethod() + " ----------");

        /* 1.请求相关数据 操作 */
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        /* 2.非拦截路由 检查 */
        if (String.format(LOGIN_PAGE_URI, contextPath).equals(uri)
                || String.format(USER_LOGIN_URI, contextPath).equals(uri)) {
            return true;
        }

        /* 3.Token 检查 */
        User user = (User) request.getSession().getAttribute(Constant.USER);
        if (null == user) {
            if (uri.contains("/api")) {
//                ObjectMapper om = new ObjectMapper();
//                PrintWriter out = response.getWriter();
//                out.print(om.writeValueAsString(new Response<>(StatusCode.USER_LOGIN_TIMEOUT)));
//                out.flush();
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logger.info("NemoInterceptor: postHandle ----------");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        logger.info("NemoInterceptor: afterCompletion ----------");
    }
}

