
package student;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Search;

import java.io.*;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetRecordServlet extends HttpServlet {
	String userName;
	String password;
	List<String> list = new ArrayList<String>();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String Cname;
		// 获取流传来的数据
		String data = getDataFromRequest(request);
		
		System.out.println("data=" + data);
		if (data.length() == 0) {
			System.out.println("nodata");
		} else {
			// 将数据转化为Json
			JSONObject object = JSONObject.fromObject(data);
			String Sid = object.getString("Sid");// 取出String类型
			int type = object.getInt("type");
			System.out.println("学号：" + Sid);
			System.out.println("type=" + type);

			if (type == 1) {//type为1时返回所有记录
				try {
					ResultSet rs = new Search().getSrecord(Sid);
					String record = null;
					while (rs.next()) {
						Cname = rs.getString("C_name");
						int num = rs.getInt("num");
						String s = Cname + ";" + num;
						if (record == null)
							record = s + ",";
						else
							record += s + ",";

					}
					System.out.println(record);
					returntoClient(response, record);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(type==0){//type为0时返回请求的1课程记录
				String coursename=object.getString("coursename");
				try {
					int num=new Search().findrecord(coursename, Sid);
					System.out.println(num);
					returntoClient(response,num+"");
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

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
