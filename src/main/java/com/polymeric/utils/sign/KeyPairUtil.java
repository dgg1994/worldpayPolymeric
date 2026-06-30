package com.polymeric.utils.sign;

import lombok.extern.slf4j.Slf4j;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.UUID;

import com.polymeric.response.sign.KeyPairResult;


@Slf4j
public class KeyPairUtil {

	/**
	 * 生成RSA密钥对
	 * 
	 * @return KeyPair 密钥对对象
	 */
	public static KeyPair generateKeyPair() throws Exception {
		// 所有代码必须在方法内部
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // 初始化密钥长度
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * 生成密钥对并返回Base64编码的字符串
	 * 
	 * @return 包含公钥和私钥的对象
	 */
	public static KeyPairResult generateKeyPairResult() throws Exception {
		KeyPair keyPair = generateKeyPair();
		// Base64编码
		String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		KeyPairResult result = new KeyPairResult();
		result.setPublicKey(publicKey);
		result.setPrivateKey(privateKey);
		return result;
	}
	
	/**
	 * @category 生成APPID
	 * @return
	 * @throws Exception
	 */
	public static String generateAppId() throws Exception {
		// 生成appId
		String appId = "OP_" + UUID.randomUUID().toString();
		return appId;
	}
}