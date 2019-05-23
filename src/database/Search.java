package database;

import java.io.IOException;
import java.sql.*;

public class Search {

	// ��ȡѧ�ÿγ̵�ѧ����Ϣ
	public ResultSet getTotalNum(String courseName) throws ClassNotFoundException {
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		String sql = "select * from learn where C_name='" + courseName + "'";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet findfeature(String Sid) throws ClassNotFoundException {
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		String sql = "select S_feature from student where S_id='" + Sid + "'";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet search(String Sid) throws ClassNotFoundException {
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select * from student where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet getSid(String courseName) throws ClassNotFoundException {
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select S_id from learn where C_name=" + "'" + courseName + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	// ��ȡ����ѧ����id
	public ResultSet searchSid() throws ClassNotFoundException {
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select S_id from student";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public String getrecord(String courseName) throws ClassNotFoundException {
		String info = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm=null;
		ResultSet rs =null;
		try {
			stm = con.createStatement();
			String sql = "select * from c_record where C_name=" + "'" + courseName + "'" + "order by Date";
			rs= stm.executeQuery(sql);
			while (rs.next()) {
				String date = rs.getString("Date");
				String totalnum = rs.getString("totalnum");
				String realnum = rs.getString("realnum");
				String nocome = rs.getString("notcome");
				if (info == null)
					info = date + ";" + totalnum + ";" + realnum + ";" + nocome;
				else
					info = info + "," + date + ";" + totalnum + ";" + realnum + ";" + nocome;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return info;
		// TODO Auto-generated method stub

	}
	
	public String searchrecord(String courseName) throws ClassNotFoundException{
		String record=null;
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			String sql = "select * from s_record where C_name=" + "'" + courseName + "'"+ "order by num desc ";
			 rs= stm.executeQuery(sql);
			while (rs.next()) {
				String S_id = rs.getString("S_id");
				int num=rs.getInt("num");
				if(num!=0){
					String name=searchname(S_id);
					if(record==null)
						record=S_id+"-"+name+";"+num+",";
					else
						record+=S_id+"-"+name+";"+num+",";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return record;
	}
	
	public String  searchname(String Sid) throws ClassNotFoundException {
		ResultSet rs = null;
		String name = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select S_name from student where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
			while(rs.next()){
				name=rs.getString("S_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return name;
	}
	
	public ResultSet getSrecord(String Sid) throws ClassNotFoundException{
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select * from s_record where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
	}
	
	//����Sid��ȡfeature
	public String returnfeature(String Sid) throws ClassNotFoundException, SQLException{
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select S_feature from student where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String feature = null;
		while(rs.next()){
			feature=rs.getString("S_feature");
		}
		return feature;
		
	}
	
	public int findrecord(String coursename,String Sid) throws SQLException, ClassNotFoundException{
		ResultSet rs = null;
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ���ݲ�ѯ
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select num from s_record where S_id=" + "'" + Sid + "'"+" and C_name= "+ "'" + coursename + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = 0;
		while(rs.next()){
			num=rs.getInt("num");
		}
		return num;
	}
}
