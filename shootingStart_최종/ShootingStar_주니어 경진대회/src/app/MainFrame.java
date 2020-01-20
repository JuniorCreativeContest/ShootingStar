package app;

import fileIO.*;
import Server.*;
import DB.*;
import login.loginFrame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTree;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	static int c1 = 0;
	FileFinderFunctions findingFile = new FileFinderFunctions();
	static String loadedPath = ""; // upload했을 때, 받아올 파일의 경로
	static F[] files;

	public MainFrame() {
	}

	private JFrame frame;
	private JPanel panel;
	private JTable table;
	private String id = "";
	private JLabel idLabel;
	private JLabel currentMode;

	private JTable getTable() {
		return this.table;
	}

	private void setId(String id) {
		this.id = id;
	}

	private String getId() {
		return this.id;
	}

	public void run(String id) {
		setId(id);
		try {
			initFrame();
			setInitialValues();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class F {
		int level;
		String upperdir;
		int isDir;
		String name;
		int updated;
		String url;
		String type;
		int size;
		boolean visited;

		public F() {
			this.level = 0;
			this.upperdir = null;
			this.isDir = 0;
			this.name = null;
			this.updated = 0;
			this.url = null;
			this.type = null;
			this.size = 0;
			this.visited = false;
		}
	}

	public DefaultMutableTreeNode makeDirectory(F thisF, F[] files, int j) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(thisF.name);
		if (files.length == 0)
			node.add(new DefaultMutableTreeNode());
		for (int i = j + 1; i < files.length; i++) {
			if (!files[i].visited) {
				if (thisF.name.contentEquals(files[i].upperdir)) {
					if (files[i].isDir == 1)
						node.add(makeDirectory(files[i], files, i));
					else
						node.add(new DefaultMutableTreeNode(files[i].name));
					files[i].visited = true;
				}
			}
		}

		return node;
	}

	private DefaultMutableTreeNode makeTree(String id) {
		DBConnect db = new DBConnect();
		db.setDB(id);
		db.Connect();

		db.ExecuteQuery("select * from fileinfo;");

		int size = 0;
		try {
			/*
			 * ResultSetMetaData rsmd =null; rsmd = (ResultSetMetaData)
			 * db.getResultSet().getMetaData(); int col = rsmd.getColumnCount();
			 */
			db.getResultSet().last();
			size = db.getResultSet().getRow();
			db.getResultSet().beforeFirst();
			int k = 0;
			while (db.getResultSet().next()) {
				k++;
			}
			db.getResultSet().beforeFirst();

		} catch (Exception e) {
			System.out.println(db.getErrorLog());
		}
		files = new F[size];
		for (int i = 0; i < size; i++)
			files[i] = new F();
		try {
			for (int i = 0; db.getResultSet().next(); i++) {
				files[i].level = db.getResultSet().getInt("level");
				files[i].upperdir = db.getResultSet().getString("upperdir");
				files[i].isDir = db.getResultSet().getInt("isdir");
				files[i].name = db.getResultSet().getString("name");
				files[i].updated = db.getResultSet().getInt("updated");
				files[i].url = db.getResultSet().getString("urlfordownload");
				files[i].type = db.getResultSet().getString("type");
				files[i].size = db.getResultSet().getInt("size");
			}
		} catch (Exception e) {
			System.out.println(db.getErrorLog());
		}

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this.getId());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		node.add(root);

		for (int i = 1; i < size; i++) {
			if (!files[i].visited) {
				if (files[0].name.equals(files[i].upperdir)) {
					if (files[i].isDir == 1) {
						root.add(makeDirectory(files[i], files, i));
					} else {
						root.add(new DefaultMutableTreeNode(files[i].name));
					}
					files[i].visited = true;
				}
			}
		}
		/*
		 * db.ExecuteQuery(""); db.getResultSet(); try { while
		 * (db.getResultSet().next()) {
		 * 
		 * } } catch (Exception e) { // 쿼리문을 너가 실행했는데 오류난 경우 ex syntax 에러 }
		 */
		return node;
	}

	private void initFrame() {
		frame = new JFrame();
		panel = new JPanel();
		ButtonListener handler = new ButtonListener(this);

		int MAXCOMPONENT_HEIGHT = 10;
		int CellHeight = (int) 395 / 10;
		int n = 10;

		frame.getContentPane().add(panel);

		frame.getContentPane().setLayout(null);

		frame.setSize(1200, 900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);

		frame.setVisible(true);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		// frame.setPreferredSize(new Dimension(540,400));
		frame.setSize(1200, 900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		idLabel = new JLabel("");
		idLabel.setFont(new Font("굴림", Font.BOLD, 15));
		idLabel.setBounds(995, 16, 87, 19);
		frame.getContentPane().add(idLabel);
		idLabel.setHorizontalAlignment(JLabel.CENTER);

		currentMode = new JLabel("Com1");
		currentMode.setHorizontalAlignment(SwingConstants.CENTER);
		currentMode.setBounds(538, 18, 87, 15);
		frame.getContentPane().add(currentMode);

		JButton modechangeBT = new JButton("mode change");
		modechangeBT.setBounds(764, 14, 111, 23);
		frame.getContentPane().add(modechangeBT);
		modechangeBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentMode.getText() == "Com1")
					currentMode.setText("Com2");
				else {
					currentMode.setText("Com1");
				}
			}
		});

		JButton logoutButton = new JButton("logout");
		logoutButton.setBounds(1087, 14, 97, 23);
		frame.getContentPane().add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				loginFrame loginFrame = new loginFrame();
			}
		});

		JSeparator TopSeperator = new JSeparator();
		TopSeperator.setBounds(12, 2, 1172, 46);
		frame.getContentPane().add(TopSeperator);

		JSeparator RightSeperator = new JSeparator();
		RightSeperator.setBounds(995, 60, 177, 791);
		frame.getContentPane().add(RightSeperator);

		String[] tableTitle = { "Type", "파일 이름", "크기", "다운로드" };
		String[][] th;
		th = new String[1][4];
		th[0][0] = "Type";
		th[0][1] = "파일 이름";
		th[0][2] = "크기 (byte)";
		th[0][3] = "다운로드";

		JSeparator LeftSeperator = new JSeparator();
		LeftSeperator.setBounds(12, 46, 177, 805);
		frame.getContentPane().add(LeftSeperator);

		JButton addTypeBt = new JButton("Add new Type");
		addTypeBt.setBounds(1011, 134, 157, 23);
		frame.getContentPane().add(addTypeBt);
		addTypeBt.addActionListener(handler);

		JButton latestUploadDownloadBt = new JButton("oneclick download");
		latestUploadDownloadBt.setBounds(1011, 77, 157, 45);
		frame.getContentPane().add(latestUploadDownloadBt);
		latestUploadDownloadBt.addActionListener(handler);

		/*
		 * File me = new File("localhost/"); // 여기에 root directory(ID)를 넣을 것.
		 * DefaultMutableTreeNode root = new DefaultMutableTreeNode(me.getName());
		 * File[] file = me.listFiles(); for (int i = 0; i < file.length; i++) {
		 * DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(file[i].getName());
		 * if (file[i].isDirectory()) root.add(this.makeDirectory(file[i])); else
		 * root.add(dmt); }
		 */
		DefaultMutableTreeNode root = this.makeTree(this.getId());
		// root.add(this.makeDirectory(file[i]));
		// root.add(dmt);

		JTree tree = new JTree(root);
		tree.addTreeSelectionListener(new SelectionListener());
		tree.setBounds(26, 60, 141, 588);
		frame.getContentPane().add(tree);
		JLabel currnetTimeLabel = new JLabel("2020.01.16 15:56");
		currnetTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currnetTimeLabel.setBounds(26, 16, 141, 18);
		frame.getContentPane().add(currnetTimeLabel);
		DefaultTableModel model = new DefaultTableModel(th, tableTitle);
		table = new JTable(model);
		table.setRowSelectionAllowed(false);
		table.setBounds(203, 46, 778, 602);
		// table.setEnabled(true); ///변경
		TableTextAlignCenter(table);
		frame.getContentPane().add(table);

		JButton uploadBT = new JButton("upLoad");
		uploadBT.setBounds(886, 14, 97, 23);
		frame.getContentPane().add(uploadBT);
		uploadBT.addActionListener(handler);

	}

	private void setInitialValues() {

		idLabel.setText(getId());
	}

	private void TableTextAlignCenter(JTable table) {
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = table.getColumnModel();

		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {

			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);

		}
	}

	private void addR(String[] data) { // 새로운 행 추가
		DefaultTableModel tableModel = (DefaultTableModel) this.getTable().getModel();// 테이블에서 테이블 모델 얻기
		tableModel.addRow(data);
	}

	public void start(String id) {// 실행
		this.setId(id);
		this.initFrame();
		idLabel.setText(getId());
	}

	public DefaultMutableTreeNode makeDirectory(File f) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getName());
		File[] file = f.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isDirectory()) {
				node.add(this.makeDirectory(file[i]));
			} else
				node.add(new DefaultMutableTreeNode(file[i].getName()));
		}
		return node;
	}

	public void newTypeFrame() {

		setBounds(100, 100, 300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	class ButtonListener implements ActionListener { // 버튼에 대한 actionlistener
		JFrame frame = null;

		public ButtonListener() {

		}

		public ButtonListener(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String type = "";
			String[][] arr = new String[1][1];

			DBConnect db = new DBConnect();

			db.setDB(id);
			db.Connect();

			db.ExecuteQuery("select * from parsingrule;");
			try {
				db.getResultSet().last();

				int parsingTableSize = db.getResultSet().getRow();
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

			switch (e.getActionCommand()) {
			case "upLoad":
				// upload 눌렸을 때 할 행위 작성

				try {
					loadedPath = findingFile.jFileChooserUtil();// 파일탐색기를 실행한 후, 압축하고 그 파일의 경로를 반환해서 LoadedPath에 저장
					String loadedPastPath = findingFile.getPastPath();
					// 여기에 파일 업로드하는 함수를 추가
					JOptionPane.showMessageDialog(null, "압축성공!");
					File fileToDelete = new File(loadedPath);// 지울 파일의 객체 생성(생성한 압축 파일)
					classifySubjects classifySubjects = new classifySubjects(arr);
					String upperDir = classifySubjects.decideSubjects(loadedPastPath);// 받아온 파일의 upperDir 탐색
					UpLoad upload = new UpLoad();
					// loadedpath =string, ->요녀석을 파싱합니다.
					if (upload.uploadFile(loadedPath, getId(), upperDir)) {

						fileToDelete.delete();// 업로드 후 해당 파일 삭제

						JOptionPane.showMessageDialog(null, "업로드 성공!");
					} else {
						JOptionPane.showMessageDialog(null, "업로드 실패! 다시 시도합니다.");
						if (upload.uploadFile(loadedPath, getId(), upperDir)) {
							JOptionPane.showMessageDialog(null, "업로드 성공!");
							fileToDelete.delete();// 업로드 후 해당 파일 삭제
						} else {
							JOptionPane.showMessageDialog(null, "업로드 실패! 올릴 파일의 크기를 확인하세요 : 최대 16MB");
							fileToDelete.delete();// 업로드 후 해당 파일 삭제
						}
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "압축실패!");
				}

				break;
			case "oneclick download":
				JOptionPane.showMessageDialog(null, "oneclick download");
				downLoad down = new downLoad();
				down.download(id + "|0|0|" + currentMode.getText());
				break;
			case "Add new Type":
				JOptionPane.showMessageDialog(null, "Add new Type");
				newTypeFrame newTypeFrame = new newTypeFrame(arr, id);
				newTypeFrame.run(arr, id);
				break;
			case "":
			}
		}

	}

	class SelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			JTree tree = (JTree) e.getSource();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			String selectedNodeName = selectedNode.toString();
			DefaultTableModel m = (DefaultTableModel) table.getModel();
			while (m.getRowCount() > 1)
				m.removeRow(m.getRowCount() - 1);

			if (selectedNode.isLeaf()) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].name.equals(selectedNode.toString()))
						if (files[i].upperdir.equals(selectedNode.getParent().toString())) {
							m.insertRow(1, new String[] { files[i].type, files[i].name, Integer.toString(files[i].size),
									"click" });
						}
				}
			} else {
				if (selectedNode.getParent().toString().equals("root")) {
					for (int i = 1; i < files.length; i++) {
						System.out.println("a");
						if (files[i].upperdir.equals(selectedNode.toString())) {
							m.insertRow(1, new String[] { files[i].type, files[i].name, Integer.toString(files[i].size),
									"click" });
						}
					}
				}
			}
			table.setCellSelectionEnabled(true);
			table.addMouseListener(new ClickEvent());

		}

	}

	class ClickEvent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (c1 == 0) {
				System.out.println("!");
				JTable t = (JTable) e.getSource();
				int row = t.getSelectedRow();
				int col = t.getSelectedColumn();
				if (col == 3 && row != 0) {
					String message = id + "|" + t.getModel().getValueAt(row, 0).toString() + "|"
							+ t.getModel().getValueAt(row, 1).toString() + "|" + currentMode.getText();
					downLoad down = new downLoad();
					down.download(message);

					String a = down.tempPath();
					File target = new File(a);
					DBConnect db = new DBConnect();
					db.setDB(id);
					db.Connect();
					db.ExecuteQuery("select * from parsingrule");
					String dest = "";
					if (currentMode.getText() == "Com1") {
						try {
							while (db.getResultSet().next()) {

								if (db.getResultSet().getString("type")
										.equals(t.getModel().getValueAt(row, 0).toString())) {
									dest = db.getResultSet().getString("address1");

								}

							}
						} catch (Exception e2) {

						}
					} else {
						try {
							while (db.getResultSet().next()) {

								if (db.getResultSet().getString("type")
										.equals(t.getModel().getValueAt(row, 0).toString())) {
									dest = db.getResultSet().getString("address2");

								}

							}
						} catch (Exception e2) {

						}
					}
					if(dest=="") {
						dest = "C:/Assign";
					}
					CompressionUtil util = new CompressionUtil();
					File DEST = new File(dest);
					util.unzip(target,DEST,"UTF-8");

				}
				c1++;
			} else
				c1--;

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
