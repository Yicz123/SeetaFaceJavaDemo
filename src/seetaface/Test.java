package seetaface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import database.DBConnecting;
import database.Insert;
import database.Search;

@SuppressWarnings("restriction")
public class Test {
	static JFrame J;
	static JFrame J1;
	static String imgPath1;
	static String imgPath2;
	static CMSeetaFace[] tFaces1;
	static CMSeetaFace[] tFaces2;
	static float tSim;// ���ƶ�
	static String srcImgPath; // ԴͼƬ��ַ
	static String source;
	static HttpServletResponse re;
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	//static String tarImgPath = "/usr/java/tomcat/apache-tomcat-7.0.92/webapps/result.jpg";
	static String tarImgPath = "E:\\seetafaceJava\\SeetaFaceJavaDemo\\Picture\\result\\result.jpg"; // ���洢�ĵ�ַ
	
	static String name;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		imgPath1 = "Picture\\GDP.jpg";// ���������ͼƬ
		source="E:\\seetafaceJava\\SeetaFaceJavaDemo";
		tarImgPath=source+"\\result.jpg";
		String course = "DataBase";
		seetaTest1(imgPath1, course);
	}

	public static String test(String course,String path,String upload,HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException {
		re=response;
		source=upload;//ÿһ�α������ݿ�ͼƬ��ŵĵط�
		tarImgPath=source+"\\result.jpg";//�����ŵĵط�
		imgPath1 = path;// ���������ͼƬ
		String m = seetaTest1(imgPath1, course);
		return m;
	}

	public static String seetaTest1(String imgPath1, String course)
			throws ClassNotFoundException, SQLException, IOException {
		SeetaFace tSeetaFace = new SeetaFace();
		int m = 0;
		String Name = null;
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		
		//tSeetaFace.initModelPath("/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac");
		tSeetaFace.initModelPath("E:\\SeetaFaceEngine-windows\\x64\\Release");
	
		tFaces1 = tSeetaFace.DetectFacesPath(imgPath1);
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		//String tFeatPath="/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac/seetafeat.txt";
		String tFeatPath = "E:\\seetafeat.txt";
		
		// �������ͼƬû������ʱ,ֱ�ӷ���
		if (null == tFaces1)
			return "0" + "noface";

		for (int i = 0; i < tFaces1.length; i++) {
			FileUtils.saveFloatArray(tFaces1[i].features, tFeatPath);
			float[] tFeat = FileUtils.loadFloatArray(tFeatPath, 2048);
			Color markContentColor = null;// ��ɫ

			// ���ݿγ����ƻ�ȡѧ����ѧ��
			ResultSet rs1 = new Search().getSid(course);
			// ��ȡѧ��
			while (rs1.next()) {
				String Sid = rs1.getString("S_id");
				ResultSet rs = new Search().search(Sid);
				// ��ȡ���ݿ��е�ͼƬ�����бȶ�
				while (rs.next()) {
					if (rs.getInt("S_state") != 1) {
					    Blob blob = (Blob) rs.getBlob("S_image");
						name = rs.getString("S_name");
						File file = new File(source+"\\test.jpg");// ָ���ļ������·�����ļ���
						if (!file.exists())// �жϣ�����ļ������ڣ��򴴽��ļ�
							file.createNewFile();
						FileOutputStream fos = new FileOutputStream(file);// ����ָ����·���������ļ������
						BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());// ���õ����ļ�д������
						BufferedImage image = ImageIO.read(in);// ����ת��ΪImage����
						JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
						encoder.encode(image);
						tFaces2 = tSeetaFace.DetectFacesPath(source+"\\test.jpg");
						// ��������ֵ
						tSim = tSeetaFace.CalcSimilarity(tFaces2[0].features, tFeat);
						if (tSim >= 0.6) {// ���ƶȴ���0.6ʱ�����ݿ��ж�Ӧ�����ֱ����ͼƬ��
							//��state����Ϊ1����ʾ�ѵ�
							new Insert().setState(1, Sid);
							m++;

							markContentColor = new Color(0, 255, 0, 128); // ��ɫ��͸����
							srcImgPath = imgPath1; // ԴͼƬ��ַ
							// tarImgPath = "F:\\Picture\\result\\result.jpg";
							// // ���洢�ĵ�ַ
							// �����ֱ����ͼƬ��
							if (m == 1) {
								//System.out.println("i=" + m);
								addWaterMark(srcImgPath, tarImgPath, tFaces1[i], name, markContentColor);
							} else {
								//System.out.println("i=" + m);
								addWaterMark(tarImgPath, tarImgPath, tFaces1[i], name, markContentColor);
							}
						}
					}

				}
			}
		}

		// ��ȡѡ���ſγ�ѧ��id
		ResultSet rs = new Search().getSid(course);
		while (rs.next()) {
			String Sid = rs.getString("S_id");
			// ����ѧ��id��ȡstate
			ResultSet rs1 = new Search().search(Sid);
			while (rs1.next()) {
				int state = rs1.getInt("S_state");
				if (state == 0) {
					String str0 = rs1.getString("S_name");
					String str1=rs1.getString("S_id");
					String str=str0+"-"+str1;
					if (Name == null) {
						Name = str;
					} else {
						Name = Name + ";" + str;
					}
				}
			}
		}
		System.out.println("��ӱ�����");

		// ����ʱ
		ResultSet rs1 = new Search().searchSid();
		while (rs1.next()) {
			String sid = rs1.getString("S_id");
			new Insert().setState(0, sid);// ��ѧ����state����Ϊ0����ʾδ��
		}

		return m + "," + Name;
	}

	private static void showimg() {
		J = new JFrame();
		J.setTitle("Img1");
		J.setBounds(230, 130, 800, 800);
		J.add(new JLabel(new ImageIcon(tarImgPath)));
		J.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		J.setVisible(true);
	}

	// ��ͼƬ������
	public static void addWaterMark(String srcImgPath, String tarImgPath, CMSeetaFace tFaces, String num,
			Color markContentColor) {
		try {
			// ��ȡԭͼƬ��Ϣ
			File srcImgFile = new File(srcImgPath);// �õ��ļ�
			Image srcImg = ImageIO.read(srcImgFile);// �ļ�ת��ΪͼƬ
			int srcImgWidth = srcImg.getWidth(null);// ��ȡͼƬ�Ŀ�
			int srcImgHeight = srcImg.getHeight(null);// ��ȡͼƬ�ĸ�

			Font font = new Font("΢���ź�", Font.PLAIN, 25); // ����
			// �ӱ��
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			g.setColor(markContentColor); // ������ɫ
			g.setFont(font); // ��������

			int x = (tFaces.right + tFaces.left) / 2;
			int y = tFaces.top;
			g.drawString(num, x, y); // ��ǳ����
			g.dispose();
			// ���ͼƬ
			FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);

			outImgStream.flush();
			outImgStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}
	
	// ��ͻ��˴�����Ϣ
		private static void returntoClient(HttpServletResponse response, String content)
				throws UnsupportedEncodingException, IOException {
			response.getOutputStream().write(content.getBytes("utf-8"));
		}
}
