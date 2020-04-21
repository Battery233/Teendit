package Encryption;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Crypto {
	
	public static final String encrypt(String plainText) {
    	Key secretKey = getKey("fendo888");
    	try {
    		Cipher cipher = Cipher.getInstance("AES");
    		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    		byte[] p = plainText.getBytes("UTF-8");
    		byte[] result = cipher.doFinal(p);
    		BASE64Encoder encoder = new BASE64Encoder();
    		String encoded = encoder.encode(result);
    		return encoded;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	} 
    }
	
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
	
    public static Key getKey(String keySeed) {
    	try {  
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
            secureRandom.setSeed(keySeed.getBytes());  
            KeyGenerator generator = KeyGenerator.getInstance("AES");  
            generator.init(secureRandom);  
            return generator.generateKey();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }
	
	
	public static void main(String[] args) {
		String s = Base64Helper.fileToBase64("/Users/wyg/Desktop/upload/0701_1.jpg");
//		String s = "1302nfndiqwjj23djii32re3";
		String e = Crypto.encrypt(s);
//		System.out.println(e);
//		String e = "j3RooPEy5utbI7BtJfVaA03sqnXPx/2gLisI8+p4gng=";
		String d = Crypto.decrypt(e);
//		System.out.println(d);
		Base64Helper.base64ToFile(d, "/Users/wyg/Desktop/upload/real.jpg");
	}

}
