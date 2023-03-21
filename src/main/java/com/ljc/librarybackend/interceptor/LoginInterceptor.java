package com.ljc.librarybackend.interceptor;


import com.ljc.librarybackend.exception.CustomException;
import com.ljc.librarybackend.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 验证登录状态
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //放行OPTIONS请求
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }
        log.info(token);
        if (StringUtils.isBlank(token)) {
            log.error("token为空或token已过期");
            throw new CustomException( "登录已过期");
        }
        if (!JwtUtils.verify(token)) {
            log.error("token不正确");
            throw new CustomException( "登录失败！请重新登录！");
        }
        Integer id = JwtUtils.getClaimByName1(token, "id");
        String username = JwtUtils.getClaimByName(token, "username");
        request.getSession().setAttribute("id", id);
        request.getSession().setAttribute("username", username);
        return true;
    }
}