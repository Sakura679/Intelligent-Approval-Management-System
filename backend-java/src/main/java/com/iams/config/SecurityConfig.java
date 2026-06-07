package com.iams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security 配置类
 * 仅用于放行所有请求，认证功能完全由 JWT 拦截器处理
 * 这样可以避免与 JWT 拦截器功能重复
 *
 * @Author: 放学后海堤日记
 * @Date: 2026/5/5 18:09
 * @Desc:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（API 项目通常不需要）
                .csrf().disable()
                // 跨域处理
                .cors()
                .and()
                // 所有请求都放行，认证交给 JWT 拦截器处理
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().permitAll()
                .and()
                // 禁用 Spring Security 的默认登录页面
                .formLogin().disable()
                // 禁用 HTTP Basic 认证
                .httpBasic().disable()
                // 禁用 Session 管理（我们使用无状态的 JWT）
                .sessionManagement().disable();
    }
}
