package com.kosmos.secondhand.account.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmos.secondhand.account.controller.dto.UserRequestDto;
import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.security.jwt.TokenManager;
import com.kosmos.secondhand.account.security.service.UserService;
import com.kosmos.secondhand.account.util.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UserRequestDto.SignInDto dto;
        try {
             dto = objectMapper.readValue(request.getReader(), UserRequestDto.SignInDto.class);
        } catch (IOException e) {
            throw new BadCredentialsException("userid or password is incorrect");
        }

        if(dto == null || !StringUtils.hasText(dto.getUserid()) || !StringUtils.hasText(dto.getPassword())) {
            throw new BadCredentialsException("userid or password is incorrect");
        }

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(dto.getUserid(), dto.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = provideToken(authResult);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        UserResponseDto.SignInDto dto = UserResponseDto.SignInDto.builder()
                        .userid(authResult.getPrincipal().toString())
                .message(ResponseMessage.SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .token(token)
                .build();

        String responseBody = objectMapper.writeValueAsString(dto);
        response.getWriter().write(responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        UserResponseDto.SignInDto dto = UserResponseDto.SignInDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ResponseMessage.FAIL)
                .build();

        String responseBody = objectMapper.writeValueAsString(dto);
        response.getWriter().write(responseBody);
    }

    private String provideToken(Authentication authentication) {
        String token = userService.findTokenByUserid(authentication.getPrincipal().toString());
        if(!tokenManager.validateToken(token)) {
            return tokenManager.createToken(authentication);
        }
        return token;
    }

}
