package com.kosmos.secondhand.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmos.secondhand.account.controller.dto.UserRequestDto;
import com.kosmos.secondhand.account.util.ResponseMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("유효한 id, password가 주어질 때, response body로 200OK, Success message, validToken를 받는다")
    void test1() throws Exception {
        String id = "user1";
        String password = "1234";
        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(id, password);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(
                post("/kosmos/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(requestBody).accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("유효하지 않는 id, password가 주어질 때,  response body로 400BadRequest, Fail message를 받고 token field는 존재하지 않는다.")
    void test2() throws Exception {
        String id = "invalidUser";
        String password = "invalidPassword";
        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(id, password);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(
                post("/kosmos/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(requestBody).accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.FAIL))
                .andExpect(jsonPath("$.token").doesNotExist());

    }

    @Test
    @DisplayName("password field가 존재하지 않을 때,  response body로 400BadRequest, Fail message를 받고 token field는 존재하지 않는다.")
    void test3() throws Exception {
        String id = "invalidUser";
        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(id, null);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(
                        post("/kosmos/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(requestBody).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.FAIL))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @DisplayName("id field가 존재하지 않을 때, response body로 400BadRequest, Fail message를 받고 token field는 존재하지 않는다.")
    void test4() throws Exception {
        String password = "invalidPassword";
        UserRequestDto.SignInDto requestDto = new UserRequestDto.SignInDto(null, password);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mvc.perform(
                        post("/kosmos/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(requestBody).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.FAIL))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @DisplayName("request body가 주어지지 않을 떄, 400BadRequest, Fail message를 받고 token field는 존재하지 않는다.")
    void test5() throws Exception {
        mvc.perform(
                        post("/kosmos/signin").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.FAIL))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

}
