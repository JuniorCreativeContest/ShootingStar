package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import DB.DBConnect;
import app.CompressionUtil;

public class downLoad {
	private String downloadedPath = "";
	String id = null;
	String type = null;
	String filename = null;
	String mode = null;

	public String tempPath() {
		return this.downloadedPath;
	}

	public String mode() {
		return this.mode;
	}

	public String type() {
		return this.type;
	}

	private final static String Path = "C:/Assign";

	public void download(String message) {
		try {
			Socket socket = new Socket("127.0.0.1", 11111);
			System.out.println("서버접속 완료");

			// 데이터를 통신을 위해서 소켓의 스트림 얻기.
			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			dos.writeInt(0);

			String fileNameMsg = message;
			int idx = message.indexOf("|");
			id = message.substring(0, idx);
			String rest = message.substring(idx + 1);

			idx = rest.indexOf("|");
			type = rest.substring(0, idx);
			rest = rest.substring(idx + 1);

			idx = rest.indexOf("|");
			filename = rest.substring(0, idx);
			mode = rest.substring(idx + 1);
			dos.writeUTF(id); // 쏘기
			dos.writeUTF(type); // 쏘기
			dos.writeUTF(filename); // 쏘기
			dos.writeUTF(mode); // 쏘기
			if (filename.equals("0")) {
				filename = dis.readUTF();
				type = dis.readUTF();
			}
			System.out.println("서버에 파일 요청을 했습니다.");
			System.out.println("서버에서 파일 데이터를 받아옵니다.");
			String tempPath = Path + "/" + filename;
			System.out.println(tempPath);
			FileOutputStream fos = new FileOutputStream(tempPath);
			// BufferedOutputStream bos = new BufferedOutputStream(fos);
			// byte[] buffer = new byte[1024];
			while (true) {
				int data = dis.read(/* buffer */);
				if (data == -1)
					break;
				fos.write(data);
			}
			downloadedPath = Path + "/filename";
			System.out.println("전송 작업이 완료되었습니다.");
			fos.close();
			dos.close();
			dis.close();
			out.close();
			in.close();
			socket.close();

			File target = new File(downloadedPath);
			DBConnect db = new DBConnect();
			db.setDB(id);
			db.Connect();
			db.ExecuteQuery("select * from parsingrule");
			String dest = "";
			if (mode.equals("Com1")) {
				try {
					while (db.getResultSet().next()) {

						if (db.getResultSet().getString("type").equals(type)) {
							dest = db.getResultSet().getString("address1");
						}
					}
				} catch (Exception e2) {

				}
			} else {
				try {
					while (db.getResultSet().next()) {

						if (db.getResultSet().getString("type").equals(type)) {
							dest = db.getResultSet().getString("address2");
						}
					}
				} catch (Exception e2) {

				}
			}
			if (dest == "") {
				dest = "C:/Assign";
			}
			
			CompressionUtil util = new CompressionUtil();
			File DEST = new File(dest);
			try {
				util.unzip(target, DEST, "UTF-8");
				target.delete();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
		} catch (Exception e) {
			downloadedPath = "";
		}
	}// main()-----------

}
