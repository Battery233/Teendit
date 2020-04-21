package Encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

public class Cryptography {
	
	Key key;
	
	public Cryptography(String str) {
		generateKey(str);
	}
	
	public void generateKey(String str) {
		try {
			KeyGenerator gene = KeyGenerator.getInstance("DES"); 
			gene.init(new SecureRandom(str.getBytes()));
			this.key = gene.generateKey();
			gene = null;
			
		} catch (Exception e) {
			throw new RuntimeException("Cause: " + e);  
		}
	}
	
	public void encrypt(String file, String destFile) throws Exception {
		Cipher cipher = Cipher.getInstance("DES"); 
		cipher.init(Cipher.ENCRYPT_MODE, this.key);  
		InputStream is = new FileInputStream(file);   
	    OutputStream out = new FileOutputStream(destFile); 
	    CipherInputStream cis = new CipherInputStream(is, cipher);
	    byte[] buffer = new byte[1024];   
	    int r;   
	    while ((r = cis.read(buffer)) > 0) {   
	        out.write(buffer, 0, r);   
	    }   
	    cis.close();   
	    is.close();   
	    out.close(); 
	}
	
	public void decrypt(String file, String dest) throws Exception {   
	    Cipher cipher = Cipher.getInstance("DES");   
	    cipher.init(Cipher.DECRYPT_MODE, this.key);   
	    InputStream is = new FileInputStream(file);   
	    OutputStream out = new FileOutputStream(dest);   
	    CipherOutputStream cos = new CipherOutputStream(out, cipher);   
	    byte[] buffer = new byte[1024];   
	    int r;   
	    while ((r = is.read(buffer)) >= 0) {   
	        System.out.println();  
	        cos.write(buffer, 0, r);   
	    }   
	    cos.close();   
	    out.close();   
	    is.close();   
	  }  

	public static void main(String[] args) throws Exception {
		Cryptography test = new Cryptography("aaa");
		//test.encrypt("/Users/wyg/Desktop/upload/0701_1.jpg", "/Users/wyg/Desktop/upload/test.jpg");
		test.decrypt("/Users/wyg/Desktop/encryption/3cf8fe81-fd9a-4d7d-b23c-91fd0f478b84.jpg", "/Users/wyg/Desktop/encryption/real.jpg");
	}
}
