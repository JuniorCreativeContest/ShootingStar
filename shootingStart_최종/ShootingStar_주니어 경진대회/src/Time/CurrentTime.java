package Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTime {
	
	private String datetime;
	public String getCurrentTime(){
	// (1) Calendar객체를 얻는다.

	Calendar cal = Calendar.getInstance();

	// (2) 출력 형태를 지정하기 위해 Formatter를 얻는다.

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd-hh:mm:ss");

	// (3) 출력형태에 맞는 문자열을 얻는다.

	datetime = sdf1.format(cal.getTime());
	return datetime;
	}
}
