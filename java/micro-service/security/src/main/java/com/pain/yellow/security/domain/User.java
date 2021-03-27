package com.pain.yellow.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Entity
@Data
@Table(name = "user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @NotNull
    @JsonIgnore
    @Column(name = "password_hash", length = 255, nullable = false)
    private String password;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String mobile;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * 账户是否未过期，默认未过期
     */
    @Builder.Default
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    /**
     * 账户是否未锁定，默认未锁定
     */
    @NotNull
    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    /**
     * 密码是否未过期，默认未过期
     */
    @NotNull
    @Builder.Default
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    /**
     * 是否启用两步验证
     */
    @Builder.Default
    @NotNull
    @Column(name = "using_mfa", nullable = false)
    private boolean usingMfa = false;

    /**
     * 两步验证的key
     */
    @JsonIgnore
    @NotNull
    @Column(name = "mfa_key", nullable = false)
    private String mfaKey;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            }
    )
    private Set<Role> authorities;
}
