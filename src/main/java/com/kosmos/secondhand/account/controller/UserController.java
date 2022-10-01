package com.kosmos.secondhand.account.controller;

import com.kosmos.secondhand.account.controller.dto.UserRequestDto;
import com.kosmos.secondhand.account.controller.dto.UserResponseDto;
import com.kosmos.secondhand.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/kosmos")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto.SignInDto> signIn(@RequestBody @Valid UserRequestDto.SignInDto signInDto) {
        UserResponseDto.SignInDto result = userService.validateCredential(signInDto);
        return ResponseEntity.ok(result);
    }

}
