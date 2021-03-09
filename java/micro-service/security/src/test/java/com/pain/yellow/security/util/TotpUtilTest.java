package com.pain.yellow.security.util;

import com.pain.yellow.security.config.AuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TotpUtilTest {

    private TotpUtil totpUtil;

    @BeforeEach
    public void setup() {
        totpUtil = new TotpUtil();
    }

    @Test
    public void givenSameKeyAndTotp_whenValidateTwice_thenFail() throws Exception {
        Instant now = Instant.now();
        Instant validFuture = now.plus(totpUtil.getTimeStep());
        Key key = totpUtil.genKey();
        String first = totpUtil.genTotp(key, now);
        Key newKey = totpUtil.genKey();
        assertTrue(totpUtil.validateTotp(key, first), "第一次验证应该成功");
        String second = totpUtil.genTotp(key, Instant.now());
        assertEquals(first, second, "时间间隔内生成的两个 TOTP 是一致的");
        String afterTimeStep = totpUtil.genTotp(key, validFuture);
        assertNotEquals(first, afterTimeStep, "过期之后和原来的 TOTP 比较应该不一致");
        assertFalse(totpUtil.validateTotp(newKey, first), "使用新的 key 验证原来的 TOTP 应该失败");
    }

    @Test
    public void givenKey_ThenEncodeAndDecodeSuccess() {
        Key key = totpUtil.genKey();
        String strKey = totpUtil.encodeKeyToString(key);
        Key decodeKey = totpUtil.decodeFromKeyString(strKey);
        assertEquals(key, decodeKey, "从字符串中获得的 key 解码后应该和原来的 key 一致");
    }
}