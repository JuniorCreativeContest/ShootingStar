import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import DB.DBConnect;

public class filetest {
	private Socket client;
	private DataInputStream din;
	private FileOutputStream out;
	private InputStream in;
	private int dataINT = 0;
	private ServerSocket soc;
	
	private int currentException = 0;
	private static final int MAX_EXCEPTION = 10;
	private final static String DEFAULTPATH = "C:\\Server";
	private final static String PATHFORDOWNLOADURL = "C:/Server";
	private boolean breakSequence = false;
	OutputStream k;
	String id;
	DataOutputStream dout;
	public synchronized void open() {

		try {
			while (true) {
				client = new Socket();

				soc = new ServerSocket(11111); 
				System.out.println("Server Ready...");
				client = soc.accept(); // 
				System.out.println("client accept!");
				
				in = null;
				out = null;
				System.out.println("Listenning...");
				in = client.getInputStream(); 
				din = new DataInputStream(in); 
				gogo();

			}
		} catch (Exception e) {
			System.out.println("Current Server socket closed...");
			try {
				soc.close();
			} catch (Exception e1) {
				if (MAX_EXCEPTION <= currentException) {
					System.out.println("Fatal Error! Shoutdown the program");
					e1.printStackTrace();
					System.exit(-1);
				}

				System.out.println("Socket closing Error..");
				currentException++;
			}
		}
	}

	public synchronized void upload() throws IOException {
		int cutIndex = 0;
		String filename = DEFAULTPATH + din.readUTF(); // String형 데이터를 전송받아 filename(파일의 이름으로 쓰일)에 저장합니다.

		/*
		 * @Parameter newdir1 : 저장될 폴더 newdir2 : 저장될 폴더의 부모 폴더 C:\부터 해당 폴더까지 경로를 모두 포함
		 * 
		 * pureDir1 : 순수한 폴더 이름 pureDir2 : 순수한 폴더 이름
		 * 
		 * cutIndex : substring을 위한 index를 저장
		 */

		System.out.println(filename);
		cutIndex = filename.lastIndexOf("\\");
		String dir1 = filename.substring(0, cutIndex);
		String pureFileName = filename.substring(cutIndex + 1);
		cutIndex = dir1.lastIndexOf("\\");
		String dir2 = dir1.substring(0, cutIndex);

		File newdir1 = new File(dir1);
		File newdir2 = new File(dir2);

		cutIndex = dir1.lastIndexOf("\\");
		String pureDir1 = dir1.substring(cutIndex + 1);

		cutIndex = dir2.lastIndexOf("\\");
		String temp = dir1.substring(cutIndex + 1);
		cutIndex = temp.lastIndexOf("\\");
		String pureDir2 = temp.substring(0, cutIndex);

		cutIndex = dir2.lastIndexOf("\\");
		id = dir2.substring(cutIndex + 1);

		DBConnect db = new DBConnect();
		db.setDB(id);
		db.Connect();
		System.out.println("DBConnected... " + db.getErrorLog());

		if (!newdir1.exists()) {
			try {
				if (!newdir2.exists()) {
					System.out.println(dir2 + " doesnt exist.. make dir...");// C:\Server\can019 생성
					System.out.println();
					newdir2.mkdir();
					System.out.println("Success : ........ " + dir2);
					System.out.println();
				}
				System.out.println(dir1 + " doesnt exist.. make dir...");// C:\Server\can019\알고리즘 생성
				System.out.println();
				newdir1.mkdir();
				System.out.println("Success : ........ " + dir1);
				System.out.println();
				System.out.print("Add to DB... :");
				db.UpdateQuery("insert into fileinfo Values(1,'root',1,'" + pureDir1 + "',200119,'NULL','" + pureDir1
						+ "',0);");
				System.out.println("Success..! ");

			} catch (Exception e) {
				System.out.println("mkdir failed... Check Permission or Dir name...");
			}
		}
		File file = new File(filename); 
		out = new FileOutputStream(file); 

		int datas = dataINT; 
		byte[] buffer = new byte[8096];
		int len; 

		for (; dataINT > 0; dataINT--) { 
			len = in.read(buffer);
			out.write(buffer, 0, len);
		}
		db.UpdateQuery("delete from fileinfo where name = '" + pureFileName + "'and type = '" + pureDir1 + "';");

		db.UpdateQuery("insert into fileinfo Values(2,'" + pureDir1 + "',0,'" + pureFileName + "',200119,'"
				+ PATHFORDOWNLOADURL + "/" + pureDir2 + "/" + pureDir1 + "/" + pureFileName + "','" + pureDir1 + "',"
				+ file.length() + ");");

		db.ExecuteQuery("Select * from latest;");
		int latestSize = 0;
		try {
			while (db.getResultSet().next()) {
				latestSize++;
			}
			db.UpdateQuery("delete from latest;");
			db.UpdateQuery("insert into latest values('" + pureFileName + "', '" + PATHFORDOWNLOADURL + "/" + pureDir2
					+ "/" + pureDir1 + "/" + pureFileName + "', '" + pureDir1 + "'," + file.length() + ");");

		} catch (Exception e) {
			System.out.println("DBError...  " + db.getErrorLog());
		}
		System.out.println("filename: " + filename + " , size : " + file.length() + " bps");

		dataINT = 0;
		out.flush();
		out.close();
		breakSequence = true;
	}

	public synchronized void gogo() throws IOException {
		try {
			while (true) {
				if (breakSequence)
					break;
				dataINT = din.readInt();
				System.out.println(dataINT);
				if (dataINT == 0) {
					
					id = din.readUTF();
					String type = din.readUTF();
					String filename = din.readUTF();
					String mode = din.readUTF();
					String filePath = null;
					
					din = new DataInputStream(in);					
					k = client.getOutputStream();
					dout = new DataOutputStream(k);
	
				/*
				 * @Parmeter
				 *
				 * received -> id|type|파일명|모드(com1,com2)
				 * id|type|파일|com1 or 
				 * can019|Algor|리폿용.zip|com1
				 * 
				 */
				// 일단 위에서 파싱이 끝났다는 가정..
				DBConnect db = new DBConnect();
				db.setDB(id);
				db.Connect();
				System.out.println("DB Connect... : +" + db.getErrorLog());
				/*******************************************************************/
				String aaaa = "";
				String bbbb = "";
				if (type.equals("0") && filename.equals("0")) { // null이 String의 null이라 수정해씀 ->걍 0으로 수정 - 유성-
					// ->latest mode
					db.ExecuteQuery("select * from latest;");
					try {
						db.getResultSet().next();
						filePath = db.getResultSet().getString("urlfordownload");
						aaaa = db.getResultSet().getString("name");
						bbbb = db.getResultSet().getString("type");
						
						// 다운로드 처리 조금있다가.
					} catch (Exception e) {
						System.out.println("Error... " + db.getErrorLog() + "... at latest mode ");
					}
				} else {
					db.ExecuteQuery("select * from fileinfo where type = '"+type+"' and name = '"+filename+"' ;");
					//잠꾸러기 유성아 여기 type이랑 filename바꿔놨는데 혹시라도 error뜨면 참고해 ㅇㅇ 일어나면
					try {
						db.getResultSet().first();
						filePath = db.getResultSet().getString("urlfordownload");
					} catch (Exception e) {
						System.out.println("!");
					}
					System.out.println("download Mode, targetFile : " + filePath);
				}
				System.out.println(filePath);
					FileInputStream fin = new FileInputStream(filePath);
					if(filename=="0");
						dout.writeUTF(aaaa);
						dout.writeUTF(bbbb);
					int n =0;
			        while(true){ 
			            int data=fin.read();
			            if(data == -1) break;	//date == 0으로 바꾼놈 누구야            
						dout.write(data);
						n++;
			        }
			        System.out.println(n);
			        fin.close();
			        dout.close();
					breakSequence = true;
				} else {
					upload();
				}
			}
		} catch (Exception e) {
			System.out.println("?");
		}
	}

}
