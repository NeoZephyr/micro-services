package com.pain.yellow.app.service;

import com.pain.yellow.app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

@RequiredArgsConstructor
public class PasswordService implements UserDetailsPasswordService {

    private final UserRepo userRepo;

    /**
     * 无缝升级密码
     *
     */
    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        return userRepo
                .findByUsername(userDetails.getUsername())
                .map(user -> (UserDetails) userRepo.save(user.withPassword(newPassword)))
                .orElse(userDetails);
    }
}
