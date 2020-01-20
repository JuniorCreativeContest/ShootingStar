package DB;

public class makeNewDB extends DBConnect{
	
	public boolean searchDB(String DBName) {
		setDB("Information_schema.SCHEMATA");
		if(Connect()==true) {
			ExecuteQuery("SELECT 1 FROM information_schema.tables WHERE table_schema = '"+DBName+"'");
			if(getResultSet().toString()=="1") {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	public boolean makeDB(String DBName) {
		
		if(this.searchDB(DBName)==false&&getErrorLog()=="NOERROR") {
			ExecuteQuery("CREATE DATABASE"+DBName);
			if(getResultSet()!=null&&getErrorLog()=="NOERROR") {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
}
