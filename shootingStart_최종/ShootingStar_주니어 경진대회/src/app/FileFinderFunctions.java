package app;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileFinderFunctions {
	private final static String afterCompressPath = "C:\\Assign\\temp";
	private static String pastPath = "";
	
	public static String jFileChooserUtil() throws IOException {

		String filePath = "";

		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
		chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
		chooser.setAcceptAllFileFilterUsed(true); // Fileter 모든 파일 적용
		chooser.setDialogTitle("저장할 파일 선택"); // 창의 제목
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드

		/*
		 * BinaryFiles부분은 그다지 쓸모가 없어 보여서 일단 빼 놓음 FileNameExtensionFilter filter = new
		 * FileNameExtensionFilter("Binary File", "cd11"); // filter 확장자 추가
		 * chooser.setFileFilter(filter); // 파일 필터를 추가
		 */

		int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈

		if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
			filePath = chooser.getSelectedFile().toString();
			pastPath = filePath;
			//System.out.println("선택한 파일의 경로 : " + filePath);

			CompressionUtil cu = new CompressionUtil(); // 파일 압축
			//cu.zip(new File(filePath));
			
			File temp1 = new File(filePath);
			File targetDir = new File(afterCompressPath);
			cu.zip(temp1,targetDir,"UTF-8",false);
			int toGetFileName = filePath.lastIndexOf("\\");
			String tempFilePath = afterCompressPath+filePath.substring(toGetFileName);
		
			
			int idx = tempFilePath.lastIndexOf(".");
			if (idx == -1) {
				filePath = tempFilePath + ".zip"; //->선택한 경로가 디렉토리일 경우 '.'이 나오질 않는다.
			} else {
				filePath = tempFilePath.substring(0, idx) + ".zip"; // 새로 만든 압축된 파일의 경로를 표시 ->여기서 string boundary 뜸
			}
	
			
			return (filePath);

		} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
			System.out.println("취소했습니다.");
			filePath = "";
		}
		return filePath;

	}
	
	public String getPastPath() {
		return pastPath;
	}
}
