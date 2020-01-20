package app;

import java.io.File;
import DB.DBConnect;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class newTypePath extends JFrame {

	String id = "";
	private JPanel contentPane;
	private JTextField PC1Address;
	private JTextField PC2Address;

	/**
	 * Launch the application.
	 */

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { newTypePath frame = new newTypePath();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	/**
	 * Create the frame.
	 */
	public newTypePath(String id) {
		this.id = id;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel addressOfPC1 = new JLabel("PC1 주소(서버)");
		addressOfPC1.setHorizontalAlignment(SwingConstants.CENTER);
		addressOfPC1.setBounds(98, 90, 72, 15);
		contentPane.add(addressOfPC1);

		JLabel addressOfPC2 = new JLabel("PC2 주소(클라이언트)");
		addressOfPC2.setHorizontalAlignment(SwingConstants.CENTER);
		addressOfPC2.setBounds(98, 120, 72, 15);
		contentPane.add(addressOfPC2);

		PC1Address = new JTextField();
		PC1Address.setBounds(195, 87, 116, 21);
		contentPane.add(PC1Address);
		PC1Address.setColumns(10);

		PC2Address = new JTextField();
		PC2Address.setBounds(195, 117, 116, 21);
		contentPane.add(PC2Address);
		PC2Address.setColumns(10);

		JButton submit = new JButton("submit");
		submit.setBounds(245, 228, 82, 23);
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBConnect db = new DBConnect();
				db.setDB(id);
				db.Connect();
				db.ExecuteQuery("select * from parsingrule;");
				try {
					db.getResultSet().last();
					String subjectName = db.getResultSet().getString("subject");
					db.UpdateQuery("update parsingrule set address1 = '"+PC1Address.getText()
					+"' where subject = '"+subjectName+"'"); //address1 추가
					
					db.UpdateQuery("update parsingrule set address2 = '"+PC2Address.getText()
					+"' where subject = '"+subjectName+"'"); //address2 추가
					JOptionPane.showMessageDialog(null,"등록되었습니다.");
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(null,"실패!... 아마 sql..문..법..");
					System.out.println(db.getErrorLog());
				}	
				dispose();
			}
		});
		contentPane.add(submit);

		JButton cancel = new JButton("cancel");
		cancel.setBounds(339, 228, 85, 23);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(cancel);
	}

	public void run(String id) {
		try {
			this.id = id;
			newTypePath frame = new newTypePath(id);
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
