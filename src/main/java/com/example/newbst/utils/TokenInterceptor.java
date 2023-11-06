package com.example.newbst.utils;

import com.example.newbst.Mapper.UserMapper;
import com.example.newbst.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Xu on 2023/8/21 16:18.
 */
    @Component
    public class TokenInterceptor implements HandlerInterceptor {
        @Autowired
        private UserMapper userMapper;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String token = request.getHeader("Authorization");
            System.out.println(token);
            // 在这里校验 Token 的有效性
            Boolean istrue=JwtToken.validateToken(token);
            if (istrue) {
                User user1=JwtToken.getUsernameFromToken(token);
                String openid=user1.getWxid();
                User user=userMapper.findopenid(openid);
                if (user != null) {
                    return true;
                } else {
                    // 返回错误响应
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Token validation failed");
                    return false;
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token validation failed");
                return false;
            }
            // 如果 Token 有效，则返回 true；否则返回 false
        }
    }
