package com.iams.interceptors;

import com.iams.context.UserContext;
import com.iams.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 12:48
 * @Desc:
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            log.warn("请求未携带 token: {}", request.getRequestURI());
            response.setStatus(401);
            return false;
        }

        // token 前缀处理
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!JwtUtils.isValidToken(token)) {
            log.warn("token 无效: {}", token);
            response.setStatus(401);
            return false;
        }

        if (JwtUtils.isTokenExpired(token)) {
            log.warn("token 已过期: {}", token);
            response.setStatus(401);
            return false;
        }

        JwtUtils.parseToken(token);

        UserContext.setCurrentUser(new UserContext(JwtUtils.getUserIdFromToken(token),
                JwtUtils.getAccountFromToken(token), token));

        if (JwtUtils.needRenewal(token)) {
            String newToken = JwtUtils.renewToken(token);
            response.setHeader("Authorization", newToken);
            log.info("token 续期成功: {}", newToken);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
        log.debug("请求结束: {}", request.getRequestURI());
    }
}
