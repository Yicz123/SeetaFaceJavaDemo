package database;

/*
 * ���ݿ�����ӣ����ⲿ�������ݿ��������캯���������ӷ���Connect����Connection
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class DBConnecting {
	String DataBase="test";
	public DBConnecting(String DataBase){
		this.DataBase=DataBase;
	}
   @SuppressWarnings("finally")
public  Connection Connect() throws ClassNotFoundException{
	   //����Connection����
	   Connection con = null;
   		//����������
	   String driver = "com.mysql.cj.jdbc.Driver";
   		//URLָ��Ҫ���ʵ����ݿ���mydata
	   String url = "jdbc:mysql://47.102.205.118:3306/myproject?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
	   //MySQL����ʱ���û���
	   String user = "root";
	   //MySQL����ʱ������
	   String password = "root";
       //������������
	  // Class.forName(driver, false, null);
       Class.forName(driver);
       //����MySQL���ݿ⣡��
       try{
    	   con = DriverManager.getConnection(url,user,password);
    	   System.out.println("��ȡ���ݿ����ӳɹ���");
       }catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           System.out.println("��ȡ���ݿ�����ʧ�ܣ�");
      }
       return con;
      
     }
   public static void main(String[] args){
	   DBConnecting db=new DBConnecting("test");
	   try {
		db.Connect();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
 
 } 