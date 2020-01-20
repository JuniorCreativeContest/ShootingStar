package app;

import javax.swing.JOptionPane;

import DB.*;

public class classifySubjects {
	String classify[][]; // classify[0][] => AL_, .java, -> algorithm | classify[1][] => WEB_, .html,
							// .css, .js -> web
							// 이차원배열에서 [][0]은 타입의 이름 [][1,2,3,4...] 는 조건 내용들
	String id = "";
	/*
	 * public classifySubjects() { // db에서 값을 받아와서 값을 집어넣어야됨 classify = new
	 * String[2][5]; classify[0][0] = "Algorithm"; classify[0][1] = "AL";
	 * classify[0][2] = ".java"; classify[1][0] = "WebProgramming"; classify[1][1] =
	 * "WEB"; classify[1][2] = ".html"; classify[1][3] = ".css"; classify[1][4] =
	 * ".js"; }
	 */

	public classifySubjects(String arr[][]) {
		classify = arr;
	}

	public classifySubjects(String arr[][], String id) {
		classify = arr;
		this.id = id;
	}

	// 과목을 분류해주는 기능

	public String decideSubjects(String transmitedSubject) {

		for (int i = 0; i < classify.length; i++) {
			for (int j = 1; j < classify[0].length; j++) {
				if (transmitedSubject.contains(classify[i][j])) {
					System.out.println("과목 번호 : " + i);
					System.out.println("과목 이름 : " + classify[i][0]);
					return classify[i][0];
				}
			}
		}

		System.out.println("분류되지 않은 과목입니다.");

		return "others";

	}

	// newType눌렀을 때

	public void newData(String type, String condition) {
		String [][] arr = new String[1][1];
		int parsingTableSize = 0;
		
		DBConnect db = new DBConnect();
		db.setDB(id);
		db.Connect();
		
		db.ExecuteQuery("select * from parsingrule;");
		try {
			db.getResultSet().last();
			
			parsingTableSize = db.getResultSet().getRow();
			db.getResultSet().beforeFirst();
			arr = new String[parsingTableSize][6];
			int i = 0;

			while (db.getResultSet().next()) {
				arr[i][0] = db.getResultSet().getString("subject");
				arr[i][1] = db.getResultSet().getString("rule1");
				arr[i][2] = db.getResultSet().getString("rule2");
				arr[i][3] = db.getResultSet().getString("rule3");
				arr[i][4] = db.getResultSet().getString("rule4");
				arr[i][5] = db.getResultSet().getString("rule5");
				i++;
			}
			
		} catch (Exception e5) {
			System.out.println(db.getErrorLog());
		}
		
		classify = arr;
		boolean discrimination = true;
		
		for(int i = 0; i < parsingTableSize; i++) {
			if(type.equals(classify[i][0])) {			//이미 존재하는 타입인지 확인
				for(int j = 1; j < 6; j++) {
					if(condition.equals(classify[i][j])) {
						JOptionPane.showMessageDialog(null,"이미 존재하는 조건입니다.");
						discrimination = false;
						return;
					}
				}
				
				if(discrimination) {
					for(int j = 0; j < 6; j++) {
						if(classify[i][j] == null) {
							
							db.UpdateQuery("update parsingrule set rule"+j+" ='"+condition+ "' where subject = '"+classify[i][0]+"';");
							
							JOptionPane.showMessageDialog(null, "조건이 추가되었습니다 : " + condition);
							return;
						}
					}
				JOptionPane.showMessageDialog(null, "조건은 최대 5개까지입니다.");
				return;
				}
			}
		}
		
		for(int i = 0; i < parsingTableSize; i++) {							//없는 타입이지만 이미 있는 조건일 경우를 검사
			for(int j = 0; j < 6; j++) {
				if(condition.equals(classify[i][j])) {
					JOptionPane.showMessageDialog(null,"이미 존재하는 조건입니다.");
					return;
				}
			}
		}
		
		db.UpdateQuery("insert parsingrule values ('"+type+"', '"+condition+"', NULL, NULL, NULL, NULL, NULL, NULL);");
		newTypePath newTypePath = new newTypePath(id);
		JOptionPane.showMessageDialog(null, "타입 : " + type + " 조건 : " + condition + "이 추가되었습니다.");
		newTypePath.run(id);
	}
}
