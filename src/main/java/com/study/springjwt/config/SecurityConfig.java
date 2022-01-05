package com.study.springjwt.config;

import com.study.springjwt.config.jwt.JwtAuthenticationFilter;
import com.study.springjwt.util.UserCostumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserCostumConverter userCostumConverter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


       http.csrf().disable();
       http.cors(); // @CrossOrigin 인증이 없을 때 사용가능, http.cors 인증이 있을 때도 사용가능
       // session을 사용하지 않음
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       // 폼 로그인 disable 되었으므로 loginProcessingUrl이 동작을 하지 않는다. 따라서 /login 찾을 수 없다고 나온다
       http.formLogin().disable()
               .addFilter(new JwtAuthenticationFilter(authenticationManager(),userCostumConverter)) //AuthenticationManager를 통해서 인증을 한다 //
               .httpBasic().disable()
               .authorizeRequests() //세션에 있어야 spring security 권한 괸리를 해준다
               .mvcMatchers("/api/v1/user/**")
               .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
               .mvcMatchers("/api/v1/manager/**")
               .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
               .mvcMatchers("/api/v1/admin/**")
               .access(" hasRole('ROLE_ADMIN')")
               .anyRequest().permitAll();


    }
}
