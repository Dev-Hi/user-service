package com.kosmos.secondhand.account.security.service;

import com.kosmos.secondhand.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String findTokenByUserid(String userid) {
        return userRepository.findByUserId(userid)
                .orElseThrow(() -> new UsernameNotFoundException("userid not found"))
                .getToken();
    }

}
