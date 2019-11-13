package in.co.indusnet.rekyc.utility;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

import javax.crypto.Cipher;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import in.co.indusnet.rekyc.exception.DataDecryptException;
import in.co.indusnet.rekyc.exception.DataEncryptException;

public class EncryptAndDecryptData {

	/*
	 * ********* this method is used for encrypting data with a key value
	 * 
	 **********/

	public static String encryptWithAESKey(String data) {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			Resource resource = new ClassPathResource("server.jks");
			keyStore.load(resource.getInputStream(), "changeit".toCharArray());
			Certificate certificate = keyStore.getCertificate("serverKey");

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
			byte[] newData = cipher.doFinal(data.getBytes());
			return new String(Base64.encodeBase64(newData));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataEncryptException("Data Encryption is not done " + e.getMessage(), 404);

		}
	}

	/*
	 * ********* this method is used for decrypting data with a key value
	 * 
	 **********/

	public static String decryptWithAESKey(String inputData) {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			Resource resource = new ClassPathResource("server.jks");
			keyStore.load(resource.getInputStream(), "changeit".toCharArray());
			Key key = keyStore.getKey("serverKey", "changeit".toCharArray());

			Cipher cipher = Cipher.getInstance("rsa/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] newData = cipher.doFinal(Base64.decodeBase64(inputData.getBytes()));
			return new String(newData);
		} catch (Exception e) {
			throw new DataDecryptException("Data Decryption is not done " + e.getMessage(), 404);
		}
	}

}
