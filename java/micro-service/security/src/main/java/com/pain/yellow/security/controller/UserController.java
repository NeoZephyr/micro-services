package com.pain.yellow.security.controller;

import com.pain.yellow.security.domain.Auth;
import com.pain.yellow.security.domain.User;
import com.pain.yellow.security.domain.dto.LoginDto;
import com.pain.yellow.security.domain.dto.SendTotpDto;
import com.pain.yellow.security.domain.dto.TotpVerificationDto;
import com.pain.yellow.security.domain.dto.UserDto;
import com.pain.yellow.security.exception.*;
import com.pain.yellow.security.service.SmsService;
import com.pain.yellow.security.service.UserCacheService;
import com.pain.yellow.security.service.impl.PasswordService;
import com.pain.yellow.security.service.impl.UserService;
import com.pain.yellow.security.util.JwtUtil;
import com.pain.yellow.security.util.MfaType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.Locale;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final JwtUtil jwtUtil;
    private final UserCacheService userCacheService;
    private final PasswordService passwordService;
    private final SmsService smsService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDto userDto, Locale locale) {
        if (userService.isUsernameExisted(userDto.getUsername())) {
            throw new DuplicateProblem("Exception.duplicate.username", messageSource, locale);
        }
        if (userService.isEmailExisted(userDto.getEmail())) {
            throw new DuplicateProblem("Exception.duplicate.email", messageSource, locale);
        }
        if (userService.isMobileExisted(userDto.getMobile())) {
            throw new DuplicateProblem("Exception.duplicate.mobile", messageSource, locale);
        }

        User user = User
                .builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobile(userDto.getMobile())
                .password(userDto.getPassword())
                .build();
        userService.register(user);
    }

    @PostMapping("/token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        return userService.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword()).map(user -> {
            passwordService.updatePassword(user, loginDto.getPassword());

            if (!user.isEnabled()) {
                throw new UserNotEnabledProblem();
            }

            if (!user.isAccountNonLocked()) {
                throw new UserAccountLockedProblem();
            }

            if (!user.isAccountNonExpired()) {
                throw new UserAccountExpiredProblem();
            }

            if (user.isUsingMfa()) {
                return ResponseEntity.ok().body(userService.login(loginDto));
            }

            String mfaId = userCacheService.cacheUser(user);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("X-Authenticate", "mfa", "realm=" + mfaId)
                    .build();
        }).orElseThrow(() -> new BadCredentialsException("username or password error"));
    }

    @PostMapping("/token/refresh")
    public Auth refresh(@RequestHeader(name = "Authorization") String authorization,
                        @RequestHeader(name = "refreshToken") String refreshToken) {
        String accessToken = authorization.replace("Bearer ", "");

        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessToken(accessToken)) {
            return new Auth(jwtUtil.genAccessTokenWithRefreshToken(refreshToken), refreshToken);
        }

        throw new AccessDeniedException("Bad Credentials");
    }

    @PutMapping("/totp")
    public void sendTotp(@Valid @RequestBody SendTotpDto sendTotpDto) {
        userCacheService.getUser(sendTotpDto.getMfaId())
                .flatMap(user ->
                        userService.createTotp(user).map(code -> Pair.of(user, code))
                ).ifPresent(pair -> {
                    if (sendTotpDto.getMfaType() == MfaType.SMS) {
                        smsService.send(pair.getLeft().getMobile(), pair.getRight());
                    } else {
                        smsService.send(pair.getLeft().getEmail(), pair.getRight());
                    }
                });
    }

    @PostMapping("/totp")
    public Auth verifyTotp(@Valid @RequestBody TotpVerificationDto totpVerificationDto) {
        return userCacheService.verifyTotp(totpVerificationDto.getMfaId(), totpVerificationDto.getCode())
                .map(User::getUsername)
                .flatMap(userService::findByUsername)
                .map(userService::loginWithTotp)
                .orElseThrow(InvalidTotpProblem::new);
    }
}
