package com.kosmos.secondhand.account.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserResponseDto {

   @Data @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   @JsonInclude(JsonInclude.Include.NON_NULL)
   public static class SignInDto {

       private int statusCode;
       private String message;
       private String token;

   }

}
