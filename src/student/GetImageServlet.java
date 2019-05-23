package student;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.cj.jdbc.Blob;

import database.Insert;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetImageServlet extends HttpServlet {
    private String path;
    private String result;
    private static String studentName;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	studentName = request.getParameter("Sid");
    	System.out.println(studentName);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setSizeThreshold(1023*1024*10);
        String upload = getServletContext().getRealPath("/");
        diskFileItemFactory.setRepository(new File(upload));
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        try{
            List<FileItem> list = servletFileUpload.parseRequest(request);

            for(FileItem item : list){
                String name = item.getName();
                System.out.println(name);
                InputStream is = item.getInputStream();
                if(name.contains("jpg")||name.contains("png")){
                    try {
                        System.out.println("aa");
                        path = upload+"\\"+item.getName();
                        inputStream2File(is, path);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(upload);
            //out.write(path);
            result = "success";
        }catch(FileUploadException e){
            e.printStackTrace();
        }


        //System.out.println("上传结果");
        //out.println(result);
        //out.flush();
        out.close();
    }
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
    public static void inputStream2File(InputStream is, String savePath) throws Exception {
    	new Insert().insert(studentName,is);
        System.out.println("savedPath:" + savePath);
        is.close();
    }
    
    //inputstream转化为byte
    public static byte[] readStream(InputStream inStream) throws Exception{	
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];	  
	    int len = -1;	
	    while((len = inStream.read(buffer)) != -1){	  
	      outStream.write(buffer, 0, len);	  
	    }	  
	    outStream.close();	  
	    inStream.close();
	    return outStream.toByteArray();	  
	  }
    public static Blob readByte(byte[] bs) throws Exception{
    	Blob blob = null;
        blob.setBytes(1, bs);
        return blob;
    }
}
