package com.pain.yellow.security.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
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

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password_hash", length = 255, nullable = false)
    private String password;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String mobile;

    @Column(length = 50)
    private String name;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

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
