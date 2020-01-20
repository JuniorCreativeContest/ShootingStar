package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
public class DBConnect {
	//private variables
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private String url;
	private String errorLog;	
	
	//Constructor
	public DBConnect() {
		con = null;
		st = null;
		rs = null;
		errorLog = "NoERROR";
	}
	//public method
	public ResultSet getResultSet() {
		return this.rs;
	}
	public String getErrorLog() {
		return this.errorLog;
	}
	public void setDB(String DBName) {
		this.url = "jdbc:mysql://localhost:3306/" + DBName + "?serverTimezone=Asia/Seoul&useSSL=false";
	}

	
	public boolean Connect() {
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
		return true;
	}
	public boolean ExecuteQuery(String Query) {
		try {
			st = con.createStatement();
			rs = st.executeQuery(Query);
			return true;
		} catch (Exception e) {
			errorLog = "SQL ERROR IN SEARCHING SEQUENCE";
			return false;
		}
	}
	public boolean UpdateQuery(String Query) {
		try {
			st = con.createStatement();
			int a = st.executeUpdate(Query);
			return true;
		}catch(Exception e) {
			errorLog = "Update ERROR IN SEARCHING SEQUENCE";
			return false;
		}
	}
}
