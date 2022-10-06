package com.kosmos.secondhand.account.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmos.secondhand.account.security.authentication.JwtAuthenticationManager;
import com.kosmos.secondhand.account.security.filter.JwtAuthenticationFilter;
import com.kosmos.secondhand.account.security.jwt.TokenManager;
import com.kosmos.secondhand.account.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final TokenManager tokenManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();

        http.formLogin().disable();
        http.addFilterAt(jwtAuthenticationFilter(
                authenticationManager(userDetailsService),
                userService,
                tokenManager,
                objectMapper()),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            UserService userService,
            TokenManager tokenManager,
            ObjectMapper objectMapper)
    {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService, tokenManager, objectMapper);
        filter.setFilterProcessesUrl("/kosmos/signin");
        return filter;
    }

    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new JwtAuthenticationManager(userDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }

}
