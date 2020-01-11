package app;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.StringTokenizer;

public class MainFrame {
    private JPanel panel1;
    private JButton pushButton;
    private JButton pullButton;
    private JTree files;
    private JTextField textField1;
    private JScrollBar scrollBar1;


    public MainFrame() {
        files = new JTree();
        TreePath tp = files.getSelectionPath();
        StringTokenizer token = new StringTokenizer(tp.toString(), "[,]");
        token.nextToken();
        if(token.hasMoreTokens()){
            String filepath = token.nextToken().trim();
        }
    }
}
