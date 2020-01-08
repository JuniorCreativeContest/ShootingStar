package app;
import login.loginFrame;
import app.MainFrame;

public class Controller implements Runnable{
	
	//private variables
	private String id = null;
	private boolean logined = false;
	private static final int INITAILSIZE = 10;
	private String[] args = new String[INITAILSIZE];
	private int size = 0;
	
	
	//private Getters&Setters
	private String getId() {
		return id;
	}
	private void setId(String id) {
		this.id = id;
	}
	
	
	private boolean logined() {
		return logined;
	}
	private void setLogined(boolean flag) {
		this.logined = flag;
	}
	
	private int size() {
		return this.size;
	}
	private void setSize(int size) {
		this.size = size;
	}
	private String[] getArgs() {
		return this.args;
	}
	private void setArgs(String data) {
		this.args[this.size()] = data;
		this.setSize(this.size()+1);
	}

	public synchronized void run() {
		
		callLoginFrame();
		
		/*loginFrame loginFrame = new loginFrame();
		loginFrame.run();
		
		while(loginFrame.getId()==null) {
			try {
				wait();
				
			}
			catch(InterruptedException e) {}
		}
		this.setId(loginFrame.getId());
			
			this.setLogined(true);
			
			this.setArgs(this.getId());
			this.setArgs(String.valueOf(this.logined));//boolean->string casting
			MainFrame mainFrame = new MainFrame();//mainFrame 객체생성
			mainFrame.start(this.getArgs());*/
	}
	public synchronized void callLoginFrame() {
		//run 과 객체를 공유하므로 synchronize 해야함
		//이를 하지 않는다면 illegalMonitorStateException 발생
		loginFrame loginFrame = new loginFrame();
		Thread th = new Thread(loginFrame);
		th.start();
		try {
			th.join();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		
		this.setId(loginFrame.getId());
	}
}
