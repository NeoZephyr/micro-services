package com.pain.blaze.common.crypto;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

    public static String encode(String key, String text, String charset) throws Exception {
        Mac instance = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(charset), "HmacSHA256");
        instance.init(secretKeySpec);
        return Hex.encodeHexString(instance.doFinal(text.getBytes(charset)));
    }

    public static String encode(String key, String text) {
        return HmacUtils.hmacSha256Hex(key, text);
    }
}