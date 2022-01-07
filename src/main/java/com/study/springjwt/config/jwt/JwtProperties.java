package com.study.springjwt.config.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    @Value("${custom.jwt.TimeMillis}")
    private Long JWT_TimeMillis;

    @Value("${custom.jwt.TOKEN_PREFIX}")
    private String JWT_TOKEN_PREFIX;

    @Value("${custom.jwt.HEADER_STRING}")
    private String JWT_HEADER_STRING;

    @Value("${custom.jwt.SECRET}")
    private String JWT_SECRET;
}
