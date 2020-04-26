package Lab4;

import java.util.ArrayList;
import java.util.Hashtable;

public class FileSystem {
	private boolean map[];
	
	private int diskPartSize;
	private int sectorSize;
	
	private Hashtable<Integer, File> files;
	private DiskPart diskPart;
	
	private int counter;
	
	public FileSystem(int _diskPartSize, int _sectorSize) throws Exception {
		diskPartSize = _diskPartSize;
		sectorSize = _sectorSize;
		
		map = new boolean[diskPartSize];
		files = new Hashtable<Integer, File>();
		diskPart = new DiskPart(diskPartSize, sectorSize);
		
		counter = 0;
		
		create("", "", "");
	}
	
	public void create(String path, String name, String extension) throws Exception {
		if (!path.isEmpty() && isFileExist(path + name + (extension.isEmpty() ? "" : ":" + extension))) {
			throw new Exception("file with same name and extension already exist in this catalog");
		}
		
		File file = new File(name, extension);
		file.addSectorNumber(reserveSector());
		files.put(counter, file);
		
		if (path.length() > 0) {
			File catalog = files.get(getFileKey(path));
			
			String append = "" + counter;
			if (read(catalog).length() > 0) {
				append = ":" + append;
			}
			
			write(catalog, read(catalog) + append);
		}
		
		counter++;
	}
	
	private int reserveSector() throws Exception {
		int answer = -1;
		
		for (int i = 0; i < diskPartSize; i++) {
			if (!map[i]) {
				answer = i;
				map[i] = true;
				break;
			}
		}
		
		if (answer == -1) {
			throw new Exception("disk is full");
		}
		
		return answer;
	}
	
	public String read(String path, String name, String extension) throws Exception {
		File file = files.get(getFileKey(path + name + (extension.isEmpty() ? "" : ":" + extension)));
		return read(file);
	}
	
	public String read(File file) {
		String data = "";
		
		ArrayList<Integer> sectorsNumbers = file.getSectorsNumbers();
		
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			data += diskPart.readDataFromSector(sectorsNumbers.get(i));
		}
		
		return data;
	}
	
	public void write(String path, String name, String extension, String data) throws Exception {
		File file = files.get(getFileKey(path + name + (extension.isEmpty() ? "" : ":" + extension)));
		write(file, data);
	}
	
	public void write(File file, String data) throws Exception {
		ArrayList<Integer> sectorsNumbers = file.getSectorsNumbers();
		
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			if (data.length() > 0) {
				diskPart.writeDataIntoSector(sectorsNumbers.get(i), data);
				data = data.substring(sectorSize < data.length() ? sectorSize : data.length(), data.length());
			} else {
				toFreeSector(sectorsNumbers.get(i));
				file.removeSectorNumber(sectorsNumbers.get(i));
				sectorsNumbers.remove(i);
				i--;
			}
		}
		
		while (data.length() > 0) {
			int additionalSectorNumber = reserveSector();
			file.addSectorNumber(additionalSectorNumber);
			diskPart.writeDataIntoSector(additionalSectorNumber, data);
			data = data.substring(sectorSize < data.length() ? sectorSize : data.length(), data.length());
		}
	}
	
	public void copy(String pathFrom, String nameFrom, String extensionFrom, String pathTo, String nameTo, String extensionTo) throws Exception {
		File fileFrom = files.get(getFileKey(pathFrom + nameFrom + (extensionFrom.isEmpty() ? "" : ":" + extensionFrom)));
		
		create(pathTo, nameTo, extensionTo);
		File fileTo = files.get(getFileKey(pathTo + nameTo + (extensionTo.isEmpty() ? "" : ":" + extensionTo)));
		
		if (extensionFrom.isEmpty() && extensionTo.isEmpty()) {
			String data = read(fileFrom);
			String[] keys = data.split(":");
			
			String newData = "";
			
			for (int i = 0; i < keys.length; i++) {
				File childFile = files.get(Integer.parseInt(keys[i]));
				copy(pathFrom + nameFrom + ">", childFile.getName(), childFile.getExtension(), pathTo + nameTo + ">", childFile.getName(), childFile.getExtension());
				
				int newFileKey = getFileKey(pathTo + nameTo + ">" + childFile.getName() + (childFile.getExtension().isEmpty() ? "" : ":" + childFile.getExtension()));
				
				if (newData.length() > 0) {
					newData += ":";
				}
				newData += newFileKey;
			}
			
			write(fileTo, newData);
		} else {
			write(fileTo, read(fileFrom));
		}
	}
	
	public void relocate(String pathFrom, String nameFrom, String extension, String pathTo, String nameTo) throws Exception {
		if (isFileExist(pathTo + nameTo + (extension.isEmpty() ? "" : ":" + extension))) {
			throw new Exception("file with same name and extension already exist in this catalog");
		}
		
		File file = files.get(getFileKey(pathFrom + nameFrom + (extension.isEmpty() ? "" : ":" + extension)));
		File catalogFrom = files.get(getFileKey(pathFrom));
		File catalogTo = files.get(getFileKey(pathTo));
		
		int key = getFileKey(pathFrom + nameFrom + (extension.isEmpty() ? "" : ":" + extension));
		String data = read(catalogFrom);
		String[] keys = data.split(":");
		
		data = "";
		for (int i = 0; i < keys.length; i++) {
			if (Integer.parseInt(keys[i]) != key) {
				data += (data.length() > 0 ? ":" : "") + keys[i];
			}
		}
		
		write(catalogFrom, data);
		data = read(catalogTo);
		write(catalogTo, data + (data.length() > 0 ? ":" : "") + key);
		
		file.setName(nameTo);
	}
	
	public void remove(String path, String name, String extension) throws Exception {
		int key = getFileKey(path + name + (extension.isEmpty() ? "" : ":" + extension));
		File file = files.get(key);
		ArrayList<Integer> sectorsNumbers = file.getSectorsNumbers();
		
		if (extension.isEmpty()) {
			String data = read(file);
			String[] keys = data.split(":");
			
			for (int i = 0; i < keys.length; i++) {
				File childFile = files.get(Integer.parseInt(keys[i]));
				remove(path + name + ">", childFile.getName(), childFile.getExtension());
			}
		}
		
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			toFreeSector(sectorsNumbers.get(i));
		}
		
		files.remove(key);
		
		if (path.length() > 0) {
			File catalog = files.get(getFileKey(path));
			String data = read(catalog);
			
			String[] keys = data.split(":");
			
			data = "";
			for (int i = 0; i < keys.length; i++) {
				if (Integer.parseInt(keys[i]) != key) {
					data += (data.length() > 0 ? ":" : "") + keys[i];
				}
			}
			
			write(catalog, data);
		}
	}
	
	public String getFileExName(int key) {
		File file = files.get(key);
		
		String name = file.getName();
		String extension = file.getExtension();
		
		return name + (extension.isEmpty() ? "" : ":" + extension);
	}
	
	private void toFreeSector(int index) {
		if (index >= 0 && index < diskPartSize) {
			map[index] = false;
		}
	}
	
	private boolean isFileExist(String fullName) {
		try {
			getFileKey(fullName);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	private int getFileKey(String fullName) throws Exception {
		if (fullName.equals(">")) {
			return 0;
		}
		
		if (fullName.endsWith(">")) {
			fullName = fullName.substring(0, fullName.length() - 1);
		}
		
		String[] path = fullName.split(">");
		
		File currentFile = files.get(0);
		int currentFileKey = 0;
		
		for (int i = 1; i < path.length; i++) {
			boolean ifFounded = false;
			
			String[] keys = read(currentFile).split(":");
			
			for (int j = 0; j < keys.length; j++) {
				if (!keys[j].isEmpty()) {
					File checkedFile = files.get(Integer.parseInt(keys[j]));
					String checkedFileName = checkedFile.getName();
					String checkedFileExtension = checkedFile.getExtension();
					
					String exName = checkedFileName + (checkedFileExtension.isEmpty() ? "" : ":" + checkedFileExtension);
					
					if (exName.equals(path[i])) {
						currentFile = files.get(Integer.parseInt(keys[j]));
						currentFileKey = Integer.parseInt(keys[j]);
						ifFounded = true;
						break;
					}
				}
			}
			
			if (!ifFounded) {
				throw new Exception("file " + path[i] + " is not exist");
			}
		}
		
		return currentFileKey;
	}
	
	public ArrayList<Integer> getFileSecotrNumbers(String path, String name, String extension) throws Exception {
		int key = getFileKey(path + name + (extension.isEmpty() ? "" : ":" + extension));
		File file = files.get(key);
		return file.getSectorsNumbers();
	}
	
	public boolean[] getMap() {
		return map.clone();
	}
}
