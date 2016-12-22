package Project1;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.awt.Color;
import java.awt.Font;

public class Music extends JPanel {
	private static final long serialVersionUID = 1L;
	private static Object[][] data = {};
	private static int lastRow;
	private static String beginTime, endTime;
	static File chosenSoundFile, chosenExcelFile;
	static JButton brw, brw1;
	static String pathExcel;
	static DefaultTableModel model;
	private static JTable table = new JTable();
	static int count = 0;
	static int selectRow;
	static String start, end;
	static Thread m1, m2;
	static JFrame frame;
	static int countThread;
	public static JTextField jt1;
	static MediaPlayer play;

	public static void getDataFromExcel(String pathExcel) {

		File file = new File(pathExcel);
		try {
			Workbook wk = Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			data = new Object[rows][cols];
			lastRow = rows;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					Cell cell = sheet.getCell(col, row);
					data[row][col] = cell.getContents();
				}

				String[] columnNames = { "Time", "SubTitile" };
				model = new DefaultTableModel(data, columnNames);
				table.setModel(model);
				table.setBounds(200, 500, 400, 398);
				table.setBackground(new Color(255, 255, 204));
				model.fireTableDataChanged();
				table.updateUI();
			}
			wk.close();
		} catch (BiffException e1) {

		} catch (IOException e) {
		}
	}

	public Music() {
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				selectRow = row;
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					if (row < lastRow) {
						beginTime = (String) data[row][0];
						endTime = "4:03";
					} else
						beginTime = endTime;
				}
				Duration a = new Duration(timeString2milisecont(beginTime));
				play.setStartTime((Duration) a);
				if (count != 0) {
					play.stop();
				}
				play.play();
				if (countThread == 1) {
					if (m1.isAlive()) {
						m1.stop();
						m2.start();
						countThread = 2;
					} else
						sub();
				}
				if (countThread == 2) {
					m2.stop();
					sub();
				} else
					sub();
			}

			private double timeString2milisecont(String timeinString) {
				String[] parts = timeinString.split(":");
				String phut = parts[0];
				String giay = parts[1];
				int intPhut = Integer.parseInt(phut);
				int intGiay = Integer.parseInt(giay);
				return intPhut * 60000 + intGiay * 1000;
			}
		});
		setLayout(null);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 100, 512, 470);
		// Add the scroll pane to this panel.
		add(scrollPane);

	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Group 1-0-1");
		frame.setBounds(100, 100, 900, 500);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ThuNguyen\\Desktop\\images.jpg"));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		Music newContentPane = new Music();
		newContentPane.setBackground(new Color(225, 255, 204));
		frame.setContentPane(newContentPane);

		// nhan hanh dong click chuot de lay file nhac
		JButton brw = new JButton("Browse Mp3 file...");
		brw.setBounds(800, 110, 151, 23);
		frame.getContentPane().add(brw);
		brw.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				chosenSoundFile = fileChooser.getSelectedFile();
				String me = "file:///" + ("" + chosenSoundFile).replace("\\", "/").replaceAll(" ", "%20");
				Media m = new Media(me);
				play = new MediaPlayer(m);
				if (count == 2) {
					play.play();
					sub();
				}
				count = 1;
			}
		});

		// hanh dong click chon file excel sau do luu vao button va nhan hanh
		// dong chay
		JButton brw1 = new JButton("Browse Excel File...");
		brw1.setBounds(800, 215, 151, 23);
		frame.getContentPane().add(brw1);
		brw1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				chosenExcelFile = fileChooser.getSelectedFile();
				String link = chosenExcelFile.getAbsolutePath();
				Music.getDataFromExcel(link);
				if (count == 1) {
					play.play();
					sub();
				}
				count = 2;
			}
		});

		// noi chua sub
		jt1 = new JTextField();
		jt1.setBounds(800, 308, 397, 68);
		frame.getContentPane().add(jt1);

		// cac thong bao chi dan
		JLabel lblSelectMusic_1 = new JLabel("Select music :");
		lblSelectMusic_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelectMusic_1.setBounds(700, 113, 97, 14);
		frame.getContentPane().add(lblSelectMusic_1);

		JLabel lblSelectMusic = new JLabel("Select excel :");
		lblSelectMusic.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelectMusic.setBounds(700, 218, 97, 17);
		frame.getContentPane().add(lblSelectMusic);

		JLabel lblNewLabel_3 = new JLabel("SubTitle :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBounds(700, 323, 84, 14);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel = new JLabel("Transcript");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(200, 50, 200, 23);
		frame.getContentPane().add(lblNewLabel);

		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null); // dat cua so giao dien chinh giua
											// man hinh
		frame.setVisible(true);

	}

	// dung de lay va hien sub tu table trancript
	public static void sub() {
		m1 = new Thread(new Runnable() {
			public void run() {
				countThread = 1;
				for (int i = selectRow; i < lastRow; i++) {
					if (i == lastRow) {
						start = (String) data[i][0];
						end = endTime;
					} else {
						start = (String) data[i][0];
						end = (String) data[i + 1][0];
					}
					jt1.setText((String) data[i][1]); // xuat sub
					// tu dong nhay dong
					Rectangle cellRect = table.getCellRect(i, 0, true);
					table.scrollRectToVisible(cellRect);
					try {
						Thread.sleep((long) (timeThreadChange(end) - timeThreadChange(start)));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		m1.start();
		m2 = new Thread(new Runnable() {
			public void run() {
				for (int i = selectRow; i < lastRow; i++) {
					if (i == lastRow - 1) {
						start = (String) data[i][0];
						end = endTime;
					} else {
						start = (String) data[i][0];
						end = (String) data[i + 1][0];
					}
					jt1.setText((String) data[i][1]);
					Rectangle cellRect = table.getCellRect(i, 0, true);
					table.scrollRectToVisible(cellRect);
					try {
						Thread.sleep((long) (timeThreadChange(end) - timeThreadChange(start)));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private static double timeThreadChange(String time) {
		String[] parts = time.split(":");
		String phut = parts[0];
		String giay = parts[1];
		int intPhut = Integer.parseInt(phut);
		int intGiay = Integer.parseInt(giay);
		return intPhut * 60000 + intGiay * 1000;
	}

	public static void main(String[] args) {
		// new JFexcel();
		new Music();
		new JFXPanel();
		// Schedule a job for the event-dispatching thread:
		// Creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
