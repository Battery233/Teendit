package Encryption;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Decryption {
	
	public static void decrypt(String inPath, String outPath) throws IOException {
		StringBuffer buffer = new StringBuffer();
		InputStream is = new FileInputStream(inPath);
        String line; 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); 
        while (line != null) { 
            buffer.append(line);
            buffer.append("\n");
            line = reader.readLine(); 
        }
        reader.close();
        is.close();
        
        
        String encrypt = buffer.toString();
        String base64 = Crypto.decrypt(encrypt);
        Base64Helper.base64ToFile(base64, outPath);
	}
	
	public static void main(String[] args) throws IOException {
		Decryption.decrypt("/Users/wyg/Desktop/encryption/2b506e33-c505-4d49-ab45-218d48133c2b(jpg).txt", "/Users/wyg/Desktop/encryption/real.jpg");
	}

}
