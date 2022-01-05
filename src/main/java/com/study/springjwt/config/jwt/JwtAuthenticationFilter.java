package com.study.springjwt.config.jwt;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springjwt.config.auth.PrincipalDetails;
import com.study.springjwt.domain.Role;
import com.study.springjwt.domain.RoleType;
import com.study.springjwt.domain.User;
import com.study.springjwt.util.UserCostumConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// /login 요청해서 username, password 전송하면(post) UsernamePasswordAuthenticationFilter 동작한다
// but formLogin().disable() 했으므로 이 필터가 동작하지 않는다 따라서 다시 필터를 등록해주어야 한다
// AuthenticationManager를 통해서 인증을 한다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserCostumConverter userCostumConverter;
    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // username, password 받아서 로그인 시도 해보는 것
        // authenticationManager로 로그인 시도를 하면 UserDetailsService 호출 (현재는 implement 한 PrincipalDetailsService 호출)
        // principalDetails를 세션에 담고 jwt 토큰을 만들어서 응답해준다 (권한 관리를 위해)
        // session에 담기지 않으면 권한 괸리가 안된다 (세션에 있어야 권한 괸리를 해준다 (authorizeRequests 안된다))

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(request.getInputStream(), Map.class);
            User user = userCostumConverter.mapToUser(map);
            // 로그인 시도를 하려면 토큰을 만들어 로그인 시도를 해야한다 // 원래는 폼로그인 기능에서 자동으로 해준다
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 실행 될때 PrincipalDetailsService의 loadUserByUsername() 함수가 실행된다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //  Principal UserDetails 객체
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principal.getUser().getUsername());
            // authentication 객체가 session 영역에 저장된다
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 이 함수가 실행된다
    // JWT 토큰을 만들어서 request요청한 사용자에게 jwt 토큰을 response 해주면 된다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

