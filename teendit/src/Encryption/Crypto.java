package Encryption;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * A class for encrypting content of a txt file. The content is the base64 String.
 */
public class Crypto {
	
	/**
	 * Encrypt a String by using AES.
	 * @param plainText
	 * @return
	 */
	public static final String encrypt(String plainText) {
    	Key secretKey = getKey("fendo888");
    	try {
    		Cipher cipher = Cipher.getInstance("AES");  // set AES
    		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    		
    		// Use byte array for AES encryption
    		byte[] p = plainText.getBytes("UTF-8");
    		byte[] result = cipher.doFinal(p);
    		BASE64Encoder encoder = new BASE64Encoder();
    		String encoded = encoder.encode(result);
    		return encoded;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	} 
    }
	
	/**
	 * Decrypt a String by using AES.
	 * @param cipherText
	 * @return
	 */
    public static final String decrypt(String cipherText) {
    	Key secretKey = getKey("fendo888");
    	try {
    		Cipher cipher = Cipher.getInstance("AES");
    		cipher.init(Cipher.DECRYPT_MODE, secretKey);
    		BASE64Decoder decoder = new BASE64Decoder();
    		byte[] c = decoder.decodeBuffer(cipherText);
    		byte[] result = cipher.doFinal(c);
    		String plainText = new String(result, "UTF-8");
    		return plainText;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
	
    /**
     * Generate the AES key based on a input string.
     * @param keySeed
     * @return
     */
    public static Key getKey(String keySeed) {
    	try {  
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");   // use SHA1 for secure ramdom.
            secureRandom.setSeed(keySeed.getBytes());  
            KeyGenerator generator = KeyGenerator.getInstance("AES");  
            generator.init(secureRandom);  
            return generator.generateKey();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }

}
