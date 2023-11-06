package com.example.newbst.utils;


import com.example.newbst.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtToken {


    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final long validityInMilliseconds = 36000000; // Token 有效期为1小时
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .claim("user", userJson(user))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static User getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return jsonToUser(claims.get("user", String.class));
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            // 获取到期时间
            long expirationTimeInMillis = claims.getExpiration().getTime();
            long currentTimeInMillis = System.currentTimeMillis();

            // 计算剩余时间
            long remainingTimeInMillis = expirationTimeInMillis - currentTimeInMillis;
            return true;
        } catch (ExpiredJwtException expiredEx) {
            // JWT 已过期
            System.out.println("Token has expired");
            return false;
        } catch (Exception e) {
            // 其他异常
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }


    private static String userJson(User user) {
        try {
//             将UserInfo对象转为JSON字符串
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }

    private static User jsonToUser(String json) {
        try {
            // 将JSON字符串转为UserInfo对象
            return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
