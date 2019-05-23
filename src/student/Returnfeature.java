package student;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Search;

import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Returnfeature extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userName;
    String course=null;
	// 连接数据库
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        this.doPost(request,responce);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
    	String Sid = request.getParameter("Sid");
		
		// 在服务器端解决中文乱码问题
		userName = new String(Sid.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("账号：" + userName);
		//根据id获取特征值
		String feature = null;
		try {
			feature=new Search().returnfeature(Sid);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(feature!=null){
			System.out.println(feature);
			returntoClient(responce,feature);
		}
    }
    
 // 向客户端传送信息
 	private void returntoClient(HttpServletResponse response, String content)
 			throws UnsupportedEncodingException, IOException {
 		response.getOutputStream().write(content.getBytes("utf-8"));
 	}
    
}
	
