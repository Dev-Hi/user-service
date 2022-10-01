package com.kosmos.secondhand.account.mock;

import com.kosmos.secondhand.account.jwt.TokenManager;
import com.kosmos.secondhand.account.repository.UserRepository;
import com.kosmos.secondhand.account.repository.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

public class MockUser {

    private final List<MockUserEntity> mockUserEntities;

    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    @Autowired
    public MockUser(TokenManager tokenManager, UserRepository userRepository) {
        mockUserEntities = new ArrayList<>();
        this.tokenManager = tokenManager;
        this.userRepository = userRepository;
    }

    public void setMockUserEntities(String userid, String password) {
        MockUserEntity data = MockUserEntity.builder()
                .userid(userid)
                .password(password)
                .build();

        data.setToken(getMockToken(userid, password));
        this.mockUserEntities.add(data);
    }

    public void saveMockUserToRepository() {
        mockUserEntities.stream().map(MockUserEntity::toUserEntity).forEach(userRepository::save);
    }

    private String getMockToken(String userId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        return tokenManager.createToken(authenticationToken);
    }

    @Builder
    @Setter @Getter
    public static class MockUserEntity {

        private String userid;
        private String password;
        private String token;

        public UserEntity toUserEntity() {
            return UserEntity.builder()
                    .userId(this.userid)
                    .password(this.password)
                    .role("user")
                    .token(this.token)
                    .build();
        }

    }

}
