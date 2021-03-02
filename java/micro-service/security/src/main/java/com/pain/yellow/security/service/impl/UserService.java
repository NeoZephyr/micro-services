package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.domain.Auth;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.repository.UserRepo;
import com.pain.yellow.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public Auth login(LoginDto loginDto) {
        return userRepo.findByUsername(loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> new Auth(jwtUtil.genAccessToken(user), jwtUtil.genRefreshToken(user)))
                .orElseThrow(() -> new BadCredentialsException("username or password error"));
    }
}
