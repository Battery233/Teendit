package rpc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import Encryption.Cryptography;
import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/uploadfile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    request.setCharacterEncoding("utf-8");
	        
		    DBConnection connection = DBConnectionFactory.getConnection();
	 
	        Boolean isMultipart=ServletFileUpload.isMultipartContent(request);
	        if (!isMultipart) {
	            return; 
	        }
	 
	        try {
	        	
	            FileItemFactory factory=new DiskFileItemFactory();
	            
	            ServletFileUpload upload=new ServletFileUpload(factory);
	            
	            List<FileItem> items=upload.parseRequest(request);
	           
	            for (FileItem item : items) {
	                String fileName = item.getFieldName();
	                if (item.isFormField()) {
	                    // control for normal form
	                    String value = item.getString("utf-8");
	                    System.out.println(fileName + "->" + value);
	                } else {
	                    // control for file uploaded
	                    String RandomName = UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(item.getName());
	                    System.out.println(fileName + "->" + FilenameUtils.getName(item.getName()));
	                    String path = "/Users/wyg/Desktop/upload/";  // To change later!!!
	                    String encryPath = "/Users/wyg/Desktop/encryption/";  // To change later!!!
	                    System.out.println(path);
	                    item.write(new File(path, RandomName)); // Store the temporary file	
	                    
	                    String newFileName = path + RandomName;
	                    Cryptography crypt = new Cryptography("aaa");  // Key which will be changed periodically.
	                    crypt.encrypt(newFileName, encryPath + RandomName);
	                    File oriFile = new File(newFileName);
	                    oriFile.delete();
	        			
	                    HttpSession session = request.getSession(false);
	        			JSONObject obj = new JSONObject();
	        			//String parentId = "456@123.com"; // To change later!!!
	        			if (session != null) {
	        				String parentId = session.getAttribute("parent_email").toString();
	        				connection.addFileName(parentId, RandomName);
	        				obj.put("status", "OK");
	        			} else {
	        				obj.put("status", "Invalid Session");
	        				response.setStatus(403);
	        			}	        				        			
	                }
	            }
	          } catch (Exception e){
	            e.printStackTrace();
	        }
	}

}
