package com.pain.blaze.common.crypto;

import java.security.MessageDigest;

public class MD5 {
    public static String encode(String text, String charset) throws Exception {
        return convertToHex(MessageDigest.getInstance("MD5").digest(text.getBytes(charset)));
    }

    public static String convertToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length; ++i) {
            String hex = Integer.toHexString(data[i] & 0xFF);

            if (hex.length() < 2) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
