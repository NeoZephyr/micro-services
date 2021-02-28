package com.pain.yellow.security.service.impl;

import com.pain.yellow.security.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordService implements UserDetailsPasswordService {

    private final UserRepo userRepo;

    /**
     * 无缝升级密码
     *
     * @param userDetails
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        return userRepo.findByUsername(userDetails.getUsername()).map(user ->
            (UserDetails) userRepo.save(user.withPassword(newPassword))
        ).orElse(userDetails);
    }
}
