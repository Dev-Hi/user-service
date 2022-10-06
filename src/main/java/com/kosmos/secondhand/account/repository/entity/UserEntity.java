package com.kosmos.secondhand.account.repository.entity;

import com.kosmos.secondhand.account.controller.role.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String token;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "RoomType", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Role> roles;

    @Column(columnDefinition = "DATE")
    private LocalDate loginDate;

    @Column(columnDefinition = "DATE")
    private LocalDate passwordSettingDate;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private boolean isAccountNonLocked;

    @Column(nullable = false)
    private boolean isAccountNonExpired;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired;

}
