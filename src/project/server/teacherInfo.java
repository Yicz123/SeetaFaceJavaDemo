/*
 * 2019/2/24
 * 老师登录和获取信息
 */
package project.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;
import net.sf.json.JSONObject;

public class teacherInfo extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String course=null;
	// 连接数据库
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

	public teacherInfo() {
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
		String userName=null;
		String passWord =null;
		String type=null;// 访问服务器时的类型，0表示登录验证，1表示获取老师信息,2表示获取老师的课程,3表示修改密码

		String data=getDataFromRequest(request);
		if (data.length() == 0) {
			System.out.println("nodata");
		} else {
			// 将数据转化为Json
			JSONObject object = JSONObject.fromObject(data);
			userName=object.getString("name");
			passWord=object.getString("pass");
			type=object.getString("type");
			
		}
		String T_Name = null;
		String password = null;
		int ct = 0;
		System.out.println("-----------------------------------");
		System.out.println("账号：" + userName);
		System.out.println("密码：" + passWord);
		System.out.println("访问类型：" + type);

		try {
			con = connect.Connect();

			// 查询老师的信息
			String sql = "select T_name from teacher where T_id=" + "'" + userName + "'";
			// 查询用户是否存在
			String sql1 = "select count(*) as ct from teacher where T_id=" + "'" + userName + "'";
			// 查询密码是否正确
			String sql2 = "select T_password  from teacher where T_id=" + "'" + userName + "'";

			/*
			 * 判断type类型
			 */

			switch (type) {
			case "0":
				// 判断用户是否存在
				PreparedStatement ps = con.prepareStatement(sql1);
				ResultSet rs1 = ps.executeQuery();
				rs1.next();
				ct = rs1.getInt("ct");
				break;
			case "1":
				// 访问类型为1时，直接确认用户存在
				ct = 1;
				break;
			case "2":
				// 返回老师教的课程
				ct = 1;
				returnCourse(response, userName);
				break;
			case "3":
				channgepassword(userName,passWord);
				break;
			}
			/*
			 * 先检查用户是否存在，再判断密码是否正确
			 */
			switch (ct) {
			// ct为0时用户不存在
			case 0:
				// 当不存在该账号时返回0
				String str = "0";
				returntoClient(response, str);
				break;

			case 1:
				// ct=1时，用户存在，检查密码是否正确
				java.sql.Statement stm1 = con.createStatement();
				ResultSet rs2 = stm1.executeQuery(sql2);
				while (rs2.next()) {
					password = rs2.getString("T_password");
				}
				String pass = password + "";
				if (pass.equals(passWord)) {
					// 类型为1时，需要返回老师信息
					if (type.equals("1")) {
						// 查询老师的信息并返回
						java.sql.Statement stm = con.createStatement();
						ResultSet rs = stm.executeQuery(sql);
						while (rs.next()) {
							T_Name = rs.getString("T_name");
							//System.out.println(rs.getString("T_name"));
						}
						course=getcourse(response,userName);
						//System.out.println(course);
						String info=course+","+T_Name;
						// 返回查询到的老师的信息和课程
						returntoClient(response, info);
					}
				} else {
					// 当密码错误时返回00
					String str1 = "00";
					returntoClient(response, str1);
				}
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void channgepassword(String name,String pass) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		new Insert().change(name,pass);
		
	}

	// 返回老师教的课程
	private void returnCourse(HttpServletResponse response, String T_id)
			throws UnsupportedEncodingException, IOException, SQLException {
		
		course=getcourse(response,T_id);
		if(null==course) course="";
		returntoClient(response, course);
	}

	private String getcourse(HttpServletResponse response, String T_id) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select C_name from course where T_id=" + "'" + T_id + "'";
		java.sql.Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		course=null;
		while (rs.next()) {
			if(course==null){
				course=rs.getString("C_name");
			}
			else
			course=course+";"+rs.getString("C_name");
		}
		return course;
	}
	
	// 获取传来的数据
			public String getDataFromRequest(HttpServletRequest request) throws IOException {
				InputStream is = request.getInputStream();
				ArrayList<Byte> arr = new ArrayList<Byte>();
				byte[] buffer = new byte[50];// 缓存数组
				int len;
				// 读取数据
				while ((len = is.read(buffer)) != -1) {
					for (int i = 0; i < len; i++) {
						arr.add(buffer[i]);
					}
				}
				byte[] src = new byte[arr.size()];
				for (int i = 0; i < src.length; i++) {
					src[i] = arr.get(i);
				}
				//加上utf-8解决中文乱码问题
				String data = new String(src,"utf-8");
				return data;
			}


	// 向客户端传送信息
	private void returntoClient(HttpServletResponse response, String content)
			throws UnsupportedEncodingException, IOException {
		response.getOutputStream().write(content.getBytes("utf-8"));
	}

}
