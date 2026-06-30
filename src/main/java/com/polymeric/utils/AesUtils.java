package com.polymeric.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.*;

public class AesUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding"; // 指定算法、模式、填充
    private static final int IV_SIZE = 16; // IV 长度 16 字节
	
    /**
     * @category AES加密
     * @param plaintext
     * @param aesKey
     * @return
     * @throws Exception
     */
	public static String encrypt(String plaintext, String aesKey) throws Exception {
		
		byte[] key = Base64.getDecoder().decode(aesKey);
		// 生成随机 IV
		byte[] iv = new byte[IV_SIZE];
		new SecureRandom().nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		// 创建密钥规范
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);

		// 初始化加密器
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

		// 执行加密（会自动处理PKCS5填充）
		byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

		// 组合 IV 和密文
		byte[] combined = new byte[iv.length + encryptedBytes.length];
		System.arraycopy(iv, 0, combined, 0, iv.length);
		System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

		return Base64.getEncoder().encodeToString(combined);
	}

	/**
	 * AES 解密
	 *
	 * @param encryptedData 包含IV和密文的Base64编码字符串
	 * @param aesKey        密钥（与加密相同）
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public static String decrypt(String encryptedData, String aesKey) throws Exception {
		byte[] key = Base64.getDecoder().decode(aesKey);
		// 解码 Base64
		byte[] combined = Base64.getDecoder().decode(encryptedData);

		// 分离 IV 和密文
		byte[] iv = new byte[IV_SIZE];
		byte[] encryptedBytes = new byte[combined.length - IV_SIZE];
		System.arraycopy(combined, 0, iv, 0, iv.length);
		System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);

		// 初始化解密器
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

		// 执行解密
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	public static void main(String[] arg) throws Exception {
		// 测试您的 Base64 格式密钥
		String base64Key = "cvGWYbem1CmjD+0x4Ju5qQChRrc0eiMHz7XkgAw1RtU=";

		String originalData = "123456";
		System.out.println("原始数据: " + originalData);

		// 加密
		String encrypted = encrypt(originalData, base64Key);
		System.out.println("加密结果: " + encrypted);

		// 解密
		String decrypted = decrypt(encrypted, base64Key);
		System.out.println("解密结果: " + decrypted);

		// 验证
		System.out.println("加解密成功: " + originalData.equals(decrypted));
	}
}