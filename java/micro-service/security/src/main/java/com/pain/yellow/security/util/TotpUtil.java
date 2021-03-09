package com.pain.yellow.security.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotpUtil {
    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_LENGTH = 6;
    private KeyGenerator keyGenerator;
    private TimeBasedOneTimePasswordGenerator totp;

    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("not found algorithm: {}", e.getLocalizedMessage());
        }
    }

    public String genTotp(Key key, Instant time) throws InvalidKeyException {
        String format = "%0" + PASSWORD_LENGTH + "d";
        return String.format(format, totp.generateOneTimePassword(key, time));
    }

    public Key genKey() {
        return keyGenerator.generateKey();
    }

    public Optional<String> genTotp(String keyStr) {
        try {
            return Optional.of(genTotp(decodeFromKeyString(keyStr), Instant.now()));
        } catch (InvalidKeyException e) {
            return Optional.empty();
        }
    }

    public boolean validateTotp(Key key, String code) throws InvalidKeyException {
        Instant now = Instant.now();
        return genTotp(key, now).equals(code);
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private String encodeKeyToString() {
        return encodeKeyToString(genKey());
    }

    public Key decodeFromKeyString(String keyStr) {
        return new SecretKeySpec(Base64.getDecoder().decode(keyStr), totp.getAlgorithm());
    }

    public Duration getTimeStep() {
        return totp.getTimeStep();
    }
}
