package login;

public class loginAttempt {
	
	protected boolean getId_Pw(String id, String pw) {
		if(id.trim().isEmpty()||pw.trim().isEmpty())
			return false;
		return true;
	}
	protected boolean loginValid() {
		return true;
	}

}
