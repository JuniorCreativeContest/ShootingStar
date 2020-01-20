package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;


public class newTypeFrame extends JFrame {
   
   String [][] data;         //db에서 넘어올 이차원 배열
   String id = "";
   private JPanel contentPane;
   private JTextField typeText;
   private JTextField conditionText;

   /**
    * Launch the application.
    */
   /*
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               newTypeFrame frame = new newTypeFrame();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }*/

   /**
    * Create the frame.
    */
   public newTypeFrame(String [][] data, String id) {
      this.id = id;
      this.data = data;
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 450, 300);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JLabel type = new JLabel("type");
      type.setHorizontalAlignment(SwingConstants.CENTER);
      type.setBounds(79, 105, 57, 15);
      contentPane.add(type);

      JLabel condition = new JLabel("condition");
      condition.setHorizontalAlignment(SwingConstants.CENTER);
      condition.setBounds(79, 130, 57, 15);
      contentPane.add(condition);

      typeText = new JTextField();
      typeText.setBounds(148, 102, 231, 21);
      contentPane.add(typeText);
      typeText.setColumns(10);

      conditionText = new JTextField();
      conditionText.setColumns(10);
      conditionText.setBounds(148, 127, 231, 21);
      contentPane.add(conditionText);

      JButton submit = new JButton("제출");
      submit.setBounds(228, 213, 86, 23);
      submit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            classifySubjects classifySubjects = new classifySubjects(data, id);
            classifySubjects.newData(typeText.getText(), conditionText.getText());
         }
      });
      contentPane.add(submit);

      JButton cancel = new JButton("취소");
      cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      cancel.setBounds(326, 213, 86, 23);
      contentPane.add(cancel);
   }

   public void run(String [][]data, String id) {
      try {
         newTypeFrame frame = new newTypeFrame(data, id);
         frame.setVisible(true);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}