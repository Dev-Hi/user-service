package com.kosmos.secondhand.account.service;

import com.kosmos.secondhand.account.controller.dto.UserRequestDto;
import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.exception.InvalidCredentialException;
import com.kosmos.secondhand.account.jwt.TokenManager;
import com.kosmos.secondhand.account.repository.UserRepository;
import com.kosmos.secondhand.account.repository.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenManager tokenManager;

    @Test
    @DisplayName("유효한 id, password가 주어질 때, token이 만료되지 않았다면, 해당 token을 발급한다.")
    void test1() {
        String id = "testuser";
        String password= "password123";
        String aliveToken = "not expired token";
        UserEntity userEntity = UserEntity.builder().userId(id).password(password).role("user").token(aliveToken).build();
        when(userRepository.findByUserIdAndPassword(id, password)).thenReturn(Optional.of(userEntity));
        when(tokenManager.validateToken(any())).thenReturn(true);

        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(id, password);
        UserResponseDto.SignInDto responseDto = userService.validateCredential(requestDto);
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getToken()).isNotNull();
        assertThat(responseDto.getToken()).isEqualTo(aliveToken);
    }

    @Test
    @DisplayName("유효한 id, password가 주어질 때, token이 만료됐다면, 새로운 token을 발급한다.")
    void test2() {
        String id = "testuser";
        String password = "password123";
        String expiredToken = "expired token";
        UserEntity userEntity = UserEntity.builder().userId(id).password(password).role("user").token(expiredToken).build();
        when(userRepository.findByUserIdAndPassword(id, password)).thenReturn(Optional.of(userEntity));
        when(tokenManager.validateToken(any())).thenReturn(false);
        when(tokenManager.createToken(any())).thenReturn("valid token");

        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(id, password);
        UserResponseDto.SignInDto responseDto = userService.validateCredential(requestDto);
        assertThat(responseDto.getToken()).isNotEqualTo(expiredToken);
        assertThat(responseDto.getToken()).isEqualTo("valid token");
    }

    @Test
    @DisplayName("유효하지 않는 id, password가 주어질 때, invalidCredentialException이 발생한다.")
    void test3() {
        String invalidId = "invalidUserid";
        String invalidPassword = "invalidPassword";
        when(userRepository.findByUserIdAndPassword(invalidId, invalidPassword)).thenThrow(InvalidCredentialException.class);

        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(invalidId, invalidPassword);
        assertThrows(InvalidCredentialException.class, () -> userService.validateCredential(requestDto));
    }

}
