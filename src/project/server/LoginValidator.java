
package project.server;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ietf.jgss.GSSName;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import database.Insert;
import database.Search;
import seetaface.Test;

public class LoginValidator extends HttpServlet {
	private String path;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginValidator() {
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

		String course = request.getParameter("courseName");
		// �ڷ������˽��������������
		course = new String(course.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("�γ�����" + course);
		System.out.println("-----------------------------------");
		// ����ѧϰ�ÿγ̵��˵���Ϣ
		try {
			ResultSet rs = new Search().getTotalNum(course);
			String allpeople = null;
			while(rs.next()){
				String Sid=rs.getString("S_id");
				String name=new Search().searchname(Sid);
				String info=name+"-"+Sid;
				if(allpeople==null) allpeople=info;
				else allpeople+="/"+info;
			}
			System.out.println(allpeople);
			returntoClient(response, allpeople);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private InputStream getimage(HttpServletResponse response,String course) throws SQLException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		// ���ݿγ����ƻ�ȡѧ����ѧ��
		InputStream is = null;
		ResultSet rs1 = new Search().getSid(course);
		// ��ȡѧ��
		while (rs1.next()) {
			String Sid = rs1.getString("S_id");
			ResultSet rs = new Search().search(Sid);
			OutputStream out;
			InputStream input;
			
			// ��ȡ���ݿ��е�ͼƬ�����бȶ�
			while (rs.next()) { 
				if (rs.getInt("S_state") != 1) {
				    Blob blob = (Blob) rs.getBlob("S_image");
					String name = rs.getString("S_name");
					is = blob.getBinaryStream();
					
					OutputStream os = response.getOutputStream();    
		            int len;
		            byte buf[] = new byte[1024];
		          
		            	while((len=is.read(buf))!=-1){
			                os.write(buf, 0, len); 
			            }
		            	is.close();
		           
		            os.close();
				}
			}
		}
		return is;
	}

	// ��ͻ��˴�����Ϣ
	private void returntoClient(HttpServletResponse response, String content)
			throws UnsupportedEncodingException, IOException {
		response.getOutputStream().write(content.getBytes("utf-8"));
	}

	// ��ת�����ַ���
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	// ��ת�����ļ�
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("�ļ�����·��Ϊ:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();

	}
}
