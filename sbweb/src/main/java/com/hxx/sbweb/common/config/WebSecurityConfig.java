//package com.hxx.sbweb.common.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * Spring Security 过滤指定路径不认证
// *
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-08-29 13:14:32
// **/
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/swagger-ui.html").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/v2/*").permitAll()
//                .antMatchers("/csrf").permitAll()
//                .antMatchers("/***").permitAll()
//                .antMatchers("/reqpara/*").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//        ;
//    }
//}
