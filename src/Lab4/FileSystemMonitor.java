package Lab4;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FileSystemMonitor extends JPanel {
	FileSystem fileSystem;
	
	private char[] map;
	
	public FileSystemMonitor(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
		update();
	}
	
	public void touchFile(String path, String name, String extension) throws Exception {
		update();
		
		ArrayList<Integer> usedSectorsNumbers = fileSystem.getFileSecotrNumbers(path, name, extension);
		for (int i = 0; i < usedSectorsNumbers.size(); i++) {
			map[usedSectorsNumbers.get(i)] = '*';
		}
	}
	
	public void update() {
		boolean[] sectorsMap = fileSystem.getMap();
		map = new char[sectorsMap.length];
		
		for (int i = 0; i < map.length; i++) {
			if (sectorsMap[i]) {
				map[i] = '|';
			} else {
				map[i] = '_';
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 1000, 1000);
		
		int x = 10;
		int y = -1;
		
		for (int i = 0; i < map.length; i++) {
			if (i % 64 == 0) {
				y += 11;
				x = 10;
			}
			
			Color color;
			switch(map[i]) {
			case '|':
				color = Color.GREEN;
				break;
			case '*':
				color = Color.RED;
				break;
			default:
				color = Color.GRAY;
				break;
			}
			
			g.setColor(color);
			g.fillRect(x, y, 10, 10);
			
			x += 11;
		}
	}

}
