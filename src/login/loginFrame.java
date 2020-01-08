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

    private final JLabel label = new JLabel("로그인");

    public String getId() {
        return this.id;
    }

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


        loginButton.setBounds(249, 81, 90, 52);
        contentPane.add(loginButton);
    }

    @Override
    public void run() {
        loginFrame loginframe = new loginFrame();
        loginframe.setVisible(true);

    }
}
