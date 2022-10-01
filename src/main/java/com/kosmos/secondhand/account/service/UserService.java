package com.kosmos.secondhand.account.service;

import com.kosmos.secondhand.account.controller.dto.UserRequestDto;
import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.exception.InvalidCredentialException;
import com.kosmos.secondhand.account.jwt.TokenManager;
import com.kosmos.secondhand.account.repository.UserRepository;
import com.kosmos.secondhand.account.repository.entity.UserEntity;
import com.kosmos.secondhand.account.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;

    public UserResponseDto.SignInDto validateCredential(UserRequestDto.SignInDto signInDto) {

        UserEntity userEntity = userRepository.findByUserIdAndPassword(signInDto.getId(), signInDto.getPassword())
                .orElseThrow(() -> new InvalidCredentialException("invalid user"));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(signInDto.getId(), signInDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = userEntity.getToken();
        if(!tokenManager.validateToken(token)) {
            token = tokenManager.createToken(authentication);
        }

        return UserResponseDto.SignInDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS)
                .token(token)
                .build();

    }

}
