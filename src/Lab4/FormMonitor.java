package Lab4;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;

public class FormMonitor {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormMonitor window = new FormMonitor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public FormMonitor() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {		
		frame = new JFrame();
		frame.setBounds(100, 100, 775, 279);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		FileSystem fileSystem = new FileSystem(1024, 16);
		FileSystemMonitor fileSystemMonitor = new FileSystemMonitor(fileSystem);
		FileManager fileManager = new FileManager(fileSystem, fileSystemMonitor);
		
		fileSystemMonitor.setBounds(10, 11, 734, 220);
		frame.getContentPane().add(fileSystemMonitor);
		frame.repaint();
		
		Timer timer = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (fileManager.invoke() == -1) {
					frame.dispose();
				}
				fileSystemMonitor.repaint();
			}
		});
		timer.start();
	}
}
