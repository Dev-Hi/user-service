package com.kosmos.secondhand.account.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());

        if(!userDetails.getPassword().equals(authentication.getCredentials())) {
            throw new BadCredentialsException("password is incorrect");
        }

        if(!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("userid " + userDetails.getUsername() + " is expired");

        }

        if(!userDetails.isAccountNonLocked()) {
            throw new LockedException("userid " + userDetails.getUsername() + " is locked");
        }

        if(!userDetails.isEnabled()) {
            throw new DisabledException("userid " + userDetails.getUsername() + " is disabled");
        }

        if(!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("password should be updated");
        }

        return new UsernamePasswordAuthenticationToken
                (
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
    }

}
