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

public class Returnimage extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userName;
    String course=null;
	// �������ݿ�
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        this.doPost(request,responce);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
    	String userName = request.getParameter("userName");
		
		// �ڷ������˽��������������
		userName = new String(userName.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("�˺ţ�" + userName);
		//��ͼƬ���ؿͻ���
		try {
			 InputStream is = getimage(responce,userName);
		        if(null!=is){  
		            OutputStream os = responce.getOutputStream();    
		            int len;
		            byte buf[] = new byte[1024];
		          
		            	while((len=is.read(buf))!=-1){
			                os.write(buf, 0, len); 
			            }
		            	is.close();
		           
		            os.close();
		        }
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	private InputStream getimage(HttpServletResponse response,String userName) throws SQLException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		// ���ݿγ����ƻ�ȡѧ����ѧ��
		InputStream is = null;
			ResultSet rs = new Search().search(userName);
			// ��ȡ���ݿ��е�ͼƬ
			while (rs.next()) {
				    Blob blob = (Blob) rs.getBlob("S_image");
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
		
		return is;
	}
}
	
