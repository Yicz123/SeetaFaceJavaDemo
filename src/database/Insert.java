package database;

/*
 * �����ݿ��в�������
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class Insert {
	public void insert(String Sid,InputStream in) throws ClassNotFoundException, FileNotFoundException {
		// ����ָ��ͼƬ�����ݿ�
		String path = "E:\\seetafaceJava\\SeetaFaceJavaDemo\\Picture\\james.jpg";
		//in = new FileInputStream(new File(path));
		// ���ݿ�����
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ��������
		PreparedStatement stm;
		try {
			stm = con.prepareStatement("update student set S_image=? where S_id=?");
			stm.setBinaryStream(1, in);
			stm.setString(2, Sid);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setState(int state, String Sid) throws ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ��������
		String sql = "update student set S_state=" + state + " where S_id=" + "'" + Sid + "'";
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
		Insert insert = new Insert();
		//insert.insert();
		/*
		 * Search search=new Search(); search.search();
		 */
	}

	public void change(String name, String pass) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ��������
		String sql = "update teacher set T_password=" + "'" + pass + "'" + " where T_id=" + "'" + name + "'";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void changeStupass(String name, String pass) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ��������
		String sql = "update student set S_password=" + "'" + pass + "'" + " where S_id=" + "'" + name + "'";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void insertresult(String coursename, String notcome, String date, int totalnum, int realnum)
			throws ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		if("null".equals(notcome)) notcome="";
		// ��������
		String sql = "INSERT INTO c_record VALUES (?,?,?,?,?)";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.setString(1, coursename);
			stm.setString(2, notcome);
			stm.setString(3, date);
			stm.setInt(4, totalnum);
			stm.setInt(5, realnum);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void feature(String Sid, String feature) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// ��������
		String sql = "update student set S_feature=" + "'" + feature + "'"+"where S_id="+"'"+Sid+"'";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	@SuppressWarnings("null")
	public void upSrecord(String nameId, String coursename) throws SQLException, ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		java.sql.Statement stm = con.createStatement();
		if (null!=nameId&&nameId.length()!=0) {
			System.out.println(nameId.length());
			String str[] = nameId.split("/");
			for (int i = 0; i < str.length; i++) {
				String str1[] = str[i].split("-");
				String sql = "select num from s_record where S_id =" + "'" + str1[1] + "'" + "and C_name =" + "'"
						+ coursename + "'";
				System.out.println(sql);
				ResultSet rs = stm.executeQuery(sql);
				while (rs.next()) {
					int num = rs.getInt("num");
					num++;
					String sql1 = "update s_record set num=" + num +" where S_id =" + "'" + str1[1] + "'"+ "and C_name =" + "'"
							+ coursename + "'";;
					System.out.println(sql1);
					DBConnecting connect1 = new DBConnecting("test");
					java.sql.Connection con1 = connect1.Connect();
					PreparedStatement stm1;
					stm1 = con1.prepareStatement(sql1);
					stm1.executeLargeUpdate();
				}

			}
		}

	}

}
