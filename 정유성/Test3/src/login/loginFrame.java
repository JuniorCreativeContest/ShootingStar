package login;

import app.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class loginFrame extends JFrame {

	private JTextField idField;
	private JPasswordField pwField;
	private JButton loginButton;

	public loginFrame() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();		
		frame.add(panel);
		
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(null);

		frame.setSize(540,400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		
		frame.setVisible(true);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		//frame.setPreferredSize(new Dimension(540,400));
		frame.setSize(400,290);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(83, 94, 57, 15);
		frame.getContentPane().add(lblId);
		
		JLabel lblPw = new JLabel("PW");
		lblPw.setBounds(83, 119, 57, 15);
		frame.getContentPane().add(lblPw);
		
		idField = new JTextField();
		idField.setBounds(125, 91, 116, 21);
		frame.getContentPane().add(idField);
		idField.setColumns(10);
		
		pwField = new JPasswordField();
		pwField.setBounds(125, 116, 116, 21);
		frame.getContentPane().add(pwField);
		pwField.setColumns(10);
		
		loginButton = new JButton("login");
		loginButton.setBounds(269, 91, 97, 46);
		
		frame.getContentPane().add(loginButton);
		
		
		loginButton.addActionListener(new ActionListener() {//익명 클래스를 이용한 login 버튼 action
			public void actionPerformed(ActionEvent e) {
				loginAttempt loginAttempt = new loginAttempt(); // loginAttempt객체생성

				String enteredId = "'"+idField.getText()+"'";//SQL QUERY문을 위해 텍스트에 ''을 붙여줌 
				String enteredPw = "";
				char[] byLetter = pwField.getPassword();// passwordField는 getText를 사용 할 수 없다
				for (char cha : byLetter) {// 다만 getPassWord는 password배열의 첫 주소를 return하므로 문자열 끝까지 for문으로 추출
					Character.toString(cha);
					enteredPw += (enteredPw.equals("")) ? "" + cha + "" : "" + cha + "";// 삼항 연산자를 통하여 공백이 나올 때 까지 pw변수를																				// 채운다.
				}
				
				enteredPw = "'"+enteredPw+"'";//SQL QUERY문을 위해 텍스트에 ''을 붙여줌			
				if (loginAttempt.getId_Pw(enteredId, enteredPw)) {//공백이 들어왔는지 확인
					if (loginAttempt.loginValid(enteredId, enteredPw)) {//DB에서 찾았을 경우
						JOptionPane.showMessageDialog(null, "성공");
						frame.dispose();
						MainFrame mainFrame = new MainFrame();
						mainFrame.run();
						
					} else {//DB에서 못찾은 경우. 
						if(loginAttempt.getErrorLog()=="NOEXCEPTION") {
							JOptionPane.showMessageDialog(null, "id/pw오류");	//단순히 못 찾은 경우
						}
						else {
							JOptionPane.showMessageDialog(null, loginAttempt.getErrorLog());//SQL EXCEPTION이라면 alert창으로 오류 문구가 호출		
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "id와 pw 모두 입력하세요");
				}

			}
		});
	}

}
