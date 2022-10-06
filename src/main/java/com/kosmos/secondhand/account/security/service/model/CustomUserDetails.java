package com.kosmos.secondhand.account.security.service.model;

import com.kosmos.secondhand.account.controller.role.Role;
import com.kosmos.secondhand.account.repository.entity.UserEntity;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private List<Role> roles;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public static UserDetails from(UserEntity userEntity) {
        return CustomUserDetails.builder()
                .userName(userEntity.getUserId())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles())
                .isAccountNonExpired(userEntity.isAccountNonExpired())
                .isAccountNonLocked(userEntity.isAccountNonLocked())
                .isCredentialsNonExpired(userEntity.isCredentialsNonExpired())
                .isEnabled(userEntity.isEnabled())
                .build();
    }

}
