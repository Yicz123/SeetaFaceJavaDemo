/*
 * 2019/2/24
 */
package project.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;
import database.Search;

public class Record extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String course=null;
	// �������ݿ�
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

	public Record() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String courseName = request.getParameter("coursename");
		// �ڷ������˽��������������
		courseName = new String(courseName.getBytes("ISO8859-1"), "UTF-8");
	
		System.out.println("-----------------------------------");
		System.out.println("�γ�����" + courseName);
		
		//�����ݿ��в�ѯ���
		try {
			String record=new Search().getrecord(courseName);
			System.out.println(record);
			if(record==null) record="";
			returntoClient(response,record);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	
	// ��ͻ��˴�����Ϣ
		private void returntoClient(HttpServletResponse response, String content)
				throws UnsupportedEncodingException, IOException {
			response.getOutputStream().write(content.getBytes("utf-8"));
		}
}
