package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class loginAttempt {

	protected String errorLog = "NOEXCEPTION";
	
	protected String getErrorLog() {
		return this.errorLog;
	}
	protected boolean getId_Pw(String id, String pw) {
		if (id.trim().isEmpty() || pw.trim().isEmpty())
			return false;
		return true;
	}

	protected boolean loginValid(String id, String pw) {
		Connection con;
		Statement st;
		ResultSet rs;
		String url = "jdbc:mysql://localhost:3306/junior_member?serverTimezone=Asia/Seoul&useSSL=false";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			errorLog = "DRIVER CONNECTION ERROR";
			return false;
		}
		try {
			con = DriverManager.getConnection(url, "root", "lhjjys1emd");
		} catch (Exception e) {
			errorLog = "DB CONNECTION ERROR";
			return false;
		}
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT * from userinfo WHERE ID = " + id + " and PW = " + pw + ";");

			if (rs.next() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			errorLog = "SQL ERROR IN SEARCHING SEQUENCE";
			return false;
		}

	}

}
