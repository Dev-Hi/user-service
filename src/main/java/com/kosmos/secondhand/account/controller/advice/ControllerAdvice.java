package com.kosmos.secondhand.account.controller.advice;

import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.exception.InvalidUserException;
import com.kosmos.secondhand.account.util.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<UserResponseDto.SignInDto> handleException(InvalidUserException exception) {
        log.warn("exception : {}", exception.toString() );
        log.warn("message : {}", exception.getMessage());
        UserResponseDto.SignInDto dto = UserResponseDto.SignInDto.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(ResponseMessage.FAIL).build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<UserResponseDto.SignInDto> handleException(HttpMessageNotReadableException exception) {
        log.warn("exception : {}", exception.toString() );
        log.warn("message : {}", exception.getMessage());
        UserResponseDto.SignInDto dto = UserResponseDto.SignInDto.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(ResponseMessage.FAIL).build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

}
