package com.kosmos.secondhand.account.security.service;

import com.kosmos.secondhand.account.repository.UserRepository;
import com.kosmos.secondhand.account.repository.entity.UserEntity;
import com.kosmos.secondhand.account.security.service.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return CustomUserDetails.from(userEntity);
    }

}
