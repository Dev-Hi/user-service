package com.kosmos.secondhand.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDto {

    @Setter @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInDto {

        private String userid;
        private String password;

    }

}
