package com.kosmos.secondhand.account.controller.advice;

import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.exception.InvalidCredentialException;
import com.kosmos.secondhand.account.exception.InvalidUserException;
import com.kosmos.secondhand.account.util.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<UserResponseDto.SignInDto> handleException(InvalidUserException invalidUserException) {
        log.warn("exception : {}", invalidUserException.toString() );
        log.warn("message : {}", invalidUserException.getMessage());
        return ResponseEntity.ok(
                UserResponseDto.SignInDto.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ResponseMessage.FAIL + " " + invalidUserException.getMessage())
                        .build()
        );
    }

}
