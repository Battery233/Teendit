package Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64Helper {
	
	 // 将 file 转化为 Base64
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
            throw new RuntimeException("文件路径无效\n" + e.getMessage());
        }
    }

    // 将 base64 转化为 file
    public static boolean base64ToFile(String base64, String path) {
        byte[] buffer;
        try {
            buffer = new BASE64Decoder().decodeBuffer(base64);
            FileOutputStream out = new FileOutputStream(path);
            out.write(buffer);
            out.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("base64字符串异常或地址异常\n" + e.getMessage());
        }
    }
	
    public static void main(String[] args) {
    	String s = Base64Helper.fileToBase64("/Users/wyg/Desktop/upload/0701_1.jpg");
    	Base64Helper.base64ToFile(s, "/Users/wyg/Desktop/upload/real.jpg");
    	System.out.println(s);
    }
}
