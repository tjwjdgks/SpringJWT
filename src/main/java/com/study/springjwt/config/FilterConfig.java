package com.study.springjwt.config;

import com.study.springjwt.filter.MyFilter;
import com.study.springjwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // filter 등록 //
    // request가 올 때 실행된다
    // security filter 체인이 동작 이후에 커스텀 필터 동작
    @Bean
    public FilterRegistrationBean<MyFilter> myFilter(){
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter());
        bean.addUrlPatterns("/*"); // 모든 요청에 대해서 다해라
        bean.setOrder(0); // 낮은 번호가 필터중에 가장 먼저 실행된다
        return bean;
    }
    @Bean
    public FilterRegistrationBean<MyFilter2> myFilter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); // 모든 요청에 대해서 다해라
        bean.setOrder(1); // 낮은 번호가 필터중에 가장 먼저 실행된다
        return bean;
    }
}
