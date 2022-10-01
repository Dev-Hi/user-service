package com.kosmos.secondhand.account.configuration;

import com.kosmos.secondhand.account.security.jwt.TokenManager;
import com.kosmos.secondhand.account.mock.MockUser;
import com.kosmos.secondhand.account.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockUserConfiguration {

    @Bean
    public MockUser mockUser(TokenManager tokenManager, UserRepository userRepository) {
        MockUser mockData = new MockUser(tokenManager, userRepository);
        mockData.setMockUserEntities("user1", "kim", "1234");
        mockData.setMockUserEntities("user2", "lee","5678");
        mockData.setMockUserEntities("user3", "park", "abcd");
        mockData.saveMockUserToRepository();
        return mockData;
    }

}
