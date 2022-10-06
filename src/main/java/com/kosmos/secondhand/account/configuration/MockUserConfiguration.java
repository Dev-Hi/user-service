package com.kosmos.secondhand.account.configuration;

import com.kosmos.secondhand.account.security.jwt.TokenManager;
import com.kosmos.secondhand.account.mock.MockUser;
import com.kosmos.secondhand.account.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MockUserConfiguration {

    @Bean
    public MockUser mockUser(TokenManager tokenManager, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        MockUser mockData = new MockUser(tokenManager, userRepository);
        mockData.setMockUserEntities("user1", "kim", passwordEncoder.encode("1234"));
        mockData.setMockUserEntities("user2", "lee", passwordEncoder.encode("5678"));
        mockData.setMockUserEntities("user3", "park", passwordEncoder.encode("abcd"));
        mockData.saveMockUserToRepository();
        return mockData;
    }

}
