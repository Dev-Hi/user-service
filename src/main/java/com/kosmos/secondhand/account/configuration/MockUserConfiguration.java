package com.kosmos.secondhand.account.configuration;

import com.kosmos.secondhand.account.jwt.TokenManager;
import com.kosmos.secondhand.account.mock.MockUser;
import com.kosmos.secondhand.account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockUserConfiguration {

    @Bean
    public MockUser mockUser(TokenManager tokenManager, UserRepository userRepository) {
        MockUser mockData = new MockUser(tokenManager, userRepository);
        mockData.setMockUserEntities("user1", "1234");
        mockData.setMockUserEntities("user2", "5678");
        mockData.setMockUserEntities("user3", "abcd");
        mockData.saveMockUserToRepository();
        return mockData;
    }

}
