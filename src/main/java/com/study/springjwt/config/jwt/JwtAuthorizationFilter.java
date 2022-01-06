package com.study.springjwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.springjwt.config.auth.PrincipalDetails;
import com.study.springjwt.domain.User;
import com.study.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

// 시큐리티 filter를 가지고 있는데 필터중에 BasicAuthenticationFilter 라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되었음
// 만약 권한이나 인증이 필요한 주소가 아닐 시 필터를 안탄다

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
        this.userRepository = userRepository;
    }
    // 인증이나 권한 필요한 요청이 있을 때 해당 필터를 타게 된다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader(jwtProperties.getJWT_HEADER_STRING());
        if(jwtHeader == null || !jwtHeader.startsWith(jwtProperties.getJWT_TOKEN_PREFIX())){
            chain.doFilter(request,response);
            return;
        }

        String jwtToken = request
                .getHeader(jwtProperties.getJWT_HEADER_STRING())
                .replace(jwtProperties.getJWT_TOKEN_PREFIX(),"");

        String username = JWT.require(Algorithm.HMAC512(jwtProperties.getJWT_SECRET()))
                .build()
                .verify(jwtToken).getClaim("username").asString();

        if(username != null){
            User userEntity = userRepository.findUserWithAllByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            // 로그인 진행 즉, UserDetailsService 사용 없이 강제로 Authentication 만드는 방법
            // password null 인 이유 UserDetailsService로 로그인 할 것이 아니기 때문에 괜찮다
            // jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체 만들어 준다
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails,null, principalDetails.getAuthorities());
            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request,response);
        }
    }
}
