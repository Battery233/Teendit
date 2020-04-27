package Encryption;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The class is used for decrypt the base64 String and change the result to the file.
 */
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
}
