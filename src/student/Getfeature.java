//获取特征值存入数据库
package student;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;
import database.Search;

import java.io.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Getfeature extends HttpServlet {
	String userName;
	String course = null;
	// 连接数据库
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
		this.doPost(request, responce);

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
		String Sid;
		
		// 获取流传来的数据
				String data = getDataFromRequest(request);
				
				System.out.println("data=" + data);
				if (data.length() == 0) {
					System.out.println("nodata");
				} else {
					// 将数据转化为Json
					JSONObject object = JSONObject.fromObject(data);
					Sid=object.getString("Sid");
					String feature=object.getString("feature");
					//存到数据库
					try {
						new Insert().feature(Sid, feature);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}

	public String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		String a = new String(b);
		return a;
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
