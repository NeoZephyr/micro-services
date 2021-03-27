package com.pain.yellow.security.service;

import com.pain.yellow.security.domain.User;
import com.pain.yellow.security.util.Constants;
import com.pain.yellow.security.util.CryptoUtil;
import com.pain.yellow.security.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCacheService {
    private final RedissonClient redissonClient;
    private final CryptoUtil cryptoUtil;
    private final TotpUtil totpUtil;

    public String cacheUser(User user) {
        String mfaId = cryptoUtil.randomAlphanumeric(12);
        RMapCache<String, User> cache = redissonClient.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId)) {
            cache.put(mfaId, user, totpUtil.getTimeStepInLong(), TimeUnit.SECONDS);
        }
        return mfaId;
    }

    public Optional<User> getUser(String mfaId) {
        RMapCache<String, User> cache = redissonClient.getMapCache(Constants.CACHE_MFA);

        if (cache.containsKey(mfaId)) {
            return Optional.of(cache.get(mfaId));
        }

        return Optional.empty();
    }

    public Optional<User> verifyTotp(String mfaId, String code) {
        RMapCache<String, User> cache = redissonClient.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId) || cache.get(mfaId) == null) {
            return Optional.empty();
        }

        User cachedUser = cache.get(mfaId);

        try {
            boolean isValid = totpUtil.validateTotp(totpUtil.decodeFromKeyString(cachedUser.getMfaKey()), code);

            if (!isValid) {
                return Optional.empty();
            }
            cache.remove(mfaId);
            return Optional.of(cachedUser);
        } catch (InvalidKeyException e) {
            log.error("Key is invalid {}", e.getLocalizedMessage());
        }
        return Optional.empty();
    }
}
