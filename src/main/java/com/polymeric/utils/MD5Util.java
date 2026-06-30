package com.polymeric.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * MD5算法
 */
public class MD5Util {
    private static final String ALGORITHM = "MD5";
    
    public static String digest(String in) {
        try {
            // 使用Java 8+的Base64编码器
            return Base64.getEncoder().encodeToString(digest(in.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static byte[] digest(byte[] in) {
        try { 
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.reset();
            return messageDigest.digest(in);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args){
        System.out.println(digest("6EpHJVo/OhmIesRC5z5chgnPrbGf03G5DW3LQj61vkJ1gONBjJrErj3TI2fRJC7zOO80RtqvpxtnpYsPnBBnQkxZ7eDQdHw4mKBmnV1rCfcveZGzy9UyjMywbej0kz4y"));
    }
}