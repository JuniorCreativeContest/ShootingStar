package login;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class loginFrame extends JFrame implements Runnable {

	private String id = null;
	private JPanel contentPane;
	private JTextField idField;
	private JPasswordField pwField;
	/**
	 * @wbp.nonvisual location=60,19
	 */
	private final JLabel label = new JLabel("로그인");

	public String getId() {
		return this.id;
	}

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					loginFrame frame = new loginFrame();
					frame.setVisible(true);
					while (id == null) {
						wait();
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}
		});

	}

	/**
	 * Create the frame.
	 */
	public loginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 385, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		idField = new JTextField();
		idField.setBounds(121, 81, 116, 21);
		contentPane.add(idField);
		idField.setColumns(10);

		pwField = new JPasswordField();
		pwField.setBounds(121, 112, 116, 21);
		contentPane.add(pwField);
		pwField.setColumns(10);

		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(52, 84, 57, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("PW");
		lblNewLabel_1.setBounds(52, 115, 57, 15);
		contentPane.add(lblNewLabel_1);

		JButton loginButton = new JButton("Login");

		loginButton.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				loginAttempt loginAttempt = new loginAttempt(); // loginAttempt객체생성

				String enteredId = idField.getText();
				String enteredPw = "";
				char[] byLetter = pwField.getPassword();// passwordField는 getText를 사용 할 수 없다
				for (char cha : byLetter) {// 다만 getPassWord는 password배열의 첫 주소를 return하므로 문자열 끝까지 for문으로 추출
					Character.toString(cha);
					enteredPw += (enteredPw.equals("")) ? "" + cha + "" : "" + cha + "";// 삼항 연산자를 통하여 공백이 나올 때 까지 pw변수를
																						// 채운다.
				}

				if (loginAttempt.getId_Pw(enteredId, enteredPw)) {
					if (loginAttempt.loginValid()) {
						id = enteredId;
						notify();
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "id/pw오류");
						id = null;
					}
				} else {
					JOptionPane.showMessageDialog(null, "id와 pw 모두 입력하세요");
					id = null;
				}

			}
		});
		loginButton.setBounds(249, 81, 90, 52);
		contentPane.add(loginButton);
	}
}
