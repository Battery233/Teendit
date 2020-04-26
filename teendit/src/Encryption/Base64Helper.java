package Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * The class used for change the base 64 String to a file.
 */
public class Base64Helper {
	
	/**
	 * Change the file to a base64 String.
	 * @param path
	 * @return
	 */
    public static String fileToBase64(String path) {
        File file = new File(path);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new BASE64Encoder().encode(buffer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Change the base64 String to a file.
     * @param base64
     * @param path
     * @return
     */
    public static boolean base64ToFile(String base64, String path) {
        byte[] buffer;
        try {
            buffer = new BASE64Decoder().decodeBuffer(base64);
            FileOutputStream out = new FileOutputStream(path);
            out.write(buffer);
            out.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
