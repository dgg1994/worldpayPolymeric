package com.polymeric.utils;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public final class GoogleAuthenticatorUtil {
	
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	 
	private static final int PASSWORD_LENGTH = 8;
	
	private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();
	
	static {
        // 配置GoogleAuthenticator使用内存存储
        gAuth.setCredentialRepository(new ICredentialRepository() {
			@Override
			public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
				
			}
			@Override
			public String getSecretKey(String userName) {
				return null;
			}
		});
    }
	
    public static String generateDefaultPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            password.append(CHAR_SET.charAt(index));
        }

        return password.toString();
    }

    // 生成用户的密钥
    public static GoogleAuthenticatorKey createKey(String userIdentifier) {
        return gAuth.createCredentials(userIdentifier);
    }

    // 将图片转换为Base64编码的字符串
    @SuppressWarnings("unused")
	private static String convertImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // 验证用户输入的验证码是否正确
    public static boolean verifyCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }
    
    public static void main(String[] args) throws IOException, WriterException {
    	String key = GoogleAuthenticatorUtil.createKey("putongyongh").getKey();
    	System.out.println(key);
    	System.out.println(GoogleAuthenticatorUtil.verifyCode("QAXMNIOPHPEMRSSV", 517037));
    	
	}
    
    
}
