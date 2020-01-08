package app;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
	
	private boolean exitFlag = false;
	private static final int INITAILSIZE = 10;
	private JPanel contentPane;
	private String[] variables = new String[INITAILSIZE];
	
	//private Getter&Setter
	private String[] getVariables() {
		return this.variables;
	}
	private boolean exitFlag() {
		return this.exitFlag; 
	}
	private void setExitFlag(boolean flag) {
		this.exitFlag = flag;
	}
	/**
	 * Launch the application.
	 */
	public boolean start(String args[]) {
		//이전에 사용하던 변수 복사
		System.arraycopy(args, 0, variables, 0, INITAILSIZE);
		
		EventQueue.invokeLater(new Runnable() {//초기화
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return this.exitFlag();
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
