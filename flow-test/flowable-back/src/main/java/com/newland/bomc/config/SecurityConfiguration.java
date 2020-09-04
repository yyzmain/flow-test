package com.newland.bomc.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * <p>Title: Security相关配置重写</p>
 * <p>Description: 解决相关默认配置问题 </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: NewLand Computer</p>
 *
 * @author linds
 * @version 1.0 developer 2018/7/27 created
 */
//@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

        http.headers().frameOptions().sameOrigin();
//                .and()
        http.csrf().disable()
                .authorizeRequests()
                //除actuator监控端点外，其他请求开启httpBasic认证
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        http.cors().disable();
 //       http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}