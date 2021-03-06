package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.domain.Auth;
import com.pain.yellow.security.domain.Role;
import com.pain.yellow.security.domain.User;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.repository.RoleRepo;
import com.pain.yellow.security.repository.UserRepo;
import com.pain.yellow.security.util.Constants;
import com.pain.yellow.security.util.JwtUtil;
import com.pain.yellow.security.util.TotpUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

// @AllArgsConstructor
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Lazy
    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final TotpUtil totpUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Auth login(LoginDto loginDto) {
        return userRepo.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> new Auth(jwtUtil.genAccessToken(user), jwtUtil.genRefreshToken(user)))
                .orElseThrow(() -> new BadCredentialsException("username or password error"));
    }

    public Auth login(UserDetails userDetails) {
        return new Auth(jwtUtil.genAccessToken(userDetails), jwtUtil.genRefreshToken(userDetails));
    }

    public Auth loginWithTotp(User user) {
        User toSave = user.withMfaKey(totpUtil.encodeKeyToString());
        User saved = saveUser(toSave);
        return login(saved);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Transactional
    public User register(User user) {
        return roleRepo.findByAuthority(Constants.ROLE_USER).map(role -> {
            Set<Role> authorities = new HashSet<>();
            authorities.add(role);
            user
//                    .withAuthorities(authorities)
                    .withPassword(passwordEncoder.encode(user.getPassword()))
                    .withMfaKey(totpUtil.encodeKeyToString());
            return userRepo.save(user);
        }).orElseThrow(() -> new BadCredentialsException("authority error"));
    }

    public boolean isUsernameExisted(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    public boolean isEmailExisted(String email) {
        return userRepo.countByEmail(email) > 0;
    }

    public boolean isMobileExisted(String mobile) {
        return userRepo.countByMobile(mobile) > 0;
    }

    public Optional<String> createTotp(User user) {
        return totpUtil.genTotp(user.getMfaKey());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void pre() {}

    @PostAuthorize("authentication.name.equals(returnObject.name)")
    public User post() {
        return null;
    }
}
