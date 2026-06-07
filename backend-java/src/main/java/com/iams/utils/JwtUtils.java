package com.iams.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 提供 Token 的生成、解析、验证、续期等功能
 *
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 13:02
 * @Desc: JWT Token 管理工具
 */
@Slf4j
public class JwtUtils {

    /** JWT 签名密钥，生产环境需要修改为复杂的密钥 */
    private static final String SECRET_KEY = "your-secret-key-here-change-in-production";

    /** Token 默认过期时间：24小时（毫秒） */
    private static final long EXPIRATION = 24 * 60 * 60 * 1000;

    /** Token 续期阈值：剩余时间小于2小时时触发续期（毫秒） */
    private static final long RENEWAL_THRESHOLD = 2 * 60 * 60 * 1000;

    /** Token 续期后的新过期时间：7天（毫秒） */
    private static final long RENEWAL_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成 JWT Token（使用默认过期时间24小时）
     *
     * @param userId 用户ID
     * @param account 用户账号
     * @param userName 用户名称
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String account, String userName) {
        return generateToken(userId, account, userName, EXPIRATION);
    }

    /**
     * 生成 JWT Token（自定义过期时间）
     *
     * @param userId 用户ID
     * @param account 用户账号
     * @param userName 用户名称
     * @param expiration 过期时间（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String account, String userName, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析 JWT Token，提取 Claims 信息
     *
     * @param token JWT Token 字符串（支持带 "Bearer " 前缀）
     * @return Claims 对象，包含用户信息
     * @throws RuntimeException Token 无效或过期时抛出异常
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token 已过期: {}", e.getMessage());
            throw new RuntimeException("Token已过期", e);
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的 Token: {}", e.getMessage());
            throw new RuntimeException("不支持的Token", e);
        } catch (MalformedJwtException e) {
            log.warn("Token 格式错误: {}", e.getMessage());
            throw new RuntimeException("Token格式错误", e);
        } catch (SignatureException e) {
            log.warn("Token 签名验证失败: {}", e.getMessage());
            throw new RuntimeException("Token签名验证失败", e);
        } catch (IllegalArgumentException e) {
            log.warn("Token 为空: {}", e.getMessage());
            throw new RuntimeException("Token不能为空", e);
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token 字符串
     * @return true-有效，false-无效
     */
    public static boolean isValidToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断 Token 是否已过期
     *
     * @param token JWT Token 字符串
     * @return true-已过期，false-未过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Token 续期
     * 当 Token 剩余时间小于阈值时，生成新的 Token（续期7天）
     * 如果 Token 已过期，则抛出异常要求重新登录
     *
     * @param token 原 JWT Token
     * @return 续期后的新 Token（如果无需续期则返回原 Token）
     * @throws RuntimeException Token 已过期或续期失败时抛出异常
     */
    public static String renewToken(String token) {
        try {
            Claims claims = parseToken(token);

            Long userId = claims.get("userId", Long.class);
            String account = claims.getSubject();
            String userName = claims.get("userName", String.class);

            Date now = new Date();
            Date originalExpiration = claims.getExpiration();
            long remainingTime = originalExpiration.getTime() - now.getTime();

            if (remainingTime > RENEWAL_THRESHOLD) {
                log.debug("Token 剩余时间充足，无需续期");
                return token;
            }

            log.info("Token 即将过期，执行续期: {}", account);
            return generateToken(userId, account, userName, RENEWAL_EXPIRATION);

        } catch (ExpiredJwtException e) {
            log.warn("Token 已过期，无法续期，需要重新登录");
            throw new RuntimeException("Token已过期，请重新登录", e);
        } catch (Exception e) {
            log.error("Token 续期失败: {}", e.getMessage());
            throw new RuntimeException("Token续期失败", e);
        }
    }

    /**
     * 从 Token 中获取用户ID
     *
     * @param token JWT Token 字符串
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中获取用户账号
     *
     * @param token JWT Token 字符串
     * @return 用户账号
     */
    public static String getAccountFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从 Token 中获取用户名称
     *
     * @param token JWT Token 字符串
     * @return 用户名称
     */
    public static String getUserNameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userName", String.class);
    }

    /**
     * 从 Token 中获取过期时间
     *
     * @param token JWT Token 字符串
     * @return 过期时间 Date 对象
     */
    public static Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 获取 Token 剩余有效时间
     *
     * @param token JWT Token 字符串
     * @return 剩余时间（毫秒），如果 Token 无效则返回 0
     */
    public static long getRemainingTime(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long remaining = expiration.getTime() - System.currentTimeMillis();
            return Math.max(remaining, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断 Token 是否需要续期
     * 当剩余时间小于等于续期阈值时需要续期
     *
     * @param token JWT Token 字符串
     * @return true-需要续期，false-不需要续期
     */
    public static boolean needRenewal(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();
            return remainingTime <= RENEWAL_THRESHOLD;
        } catch (Exception e) {
            return false;
        }
    }
}
