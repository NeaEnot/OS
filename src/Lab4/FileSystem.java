package Lab4;

import java.util.ArrayList;
import java.util.Hashtable;

public class FileSystem {
	private boolean map[];
	
	private int diskPartSize;
	private int sectorSize;
	
	private Hashtable<String, File> files;
	private DiskPart diskPart;
	
	public FileSystem(int _diskPartSize, int _sectorSize) throws Exception {
		diskPartSize = _diskPartSize;
		sectorSize = _sectorSize;
		
		map = new boolean[diskPartSize];
		files = new Hashtable<String, File>();
		diskPart = new DiskPart(diskPartSize, sectorSize);
		
		create("", "", "");
	}
	
	public void create(String path, String name, String extension) throws Exception {
		String exFileName = name + (extension.isEmpty() ? "" : ":" + extension);
		String fullFileName = path + exFileName;
		
		if (!path.isEmpty() && files.containsKey(fullFileName)) {
			throw new Exception("file with same name and extension already exist in this catalog");
		}
		
		File file = new File(name, extension);
		files.put(fullFileName, file);
		
		if (path.length() > 0) {
			File catalog = files.get(path.substring(0, path.length() - 1));
			String data = read(catalog);
			
			write(catalog, data + (data.length() > 0 ? ";" : "") + exFileName);
		}
	}
	
	public String read(String path, String name, String extension) throws Exception {
		String fullFileName = path + name + (extension.isEmpty() ? "" : ":" + extension);
		File file = files.get(fullFileName);
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
		String fullFileName = path + name + (extension.isEmpty() ? "" : ":" + extension);
		File file = files.get(fullFileName);
		write(file, data);
	}
	
	public void write(File file, String data) throws Exception {
		ArrayList<Integer> sectorsNumbers = file.getSectorsNumbers();
		int countRemoved = 0;
		
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			if (data.length() > 0) {
				diskPart.writeDataIntoSector(sectorsNumbers.get(i), data);
				data = data.substring(sectorSize < data.length() ? sectorSize : data.length(), data.length());
			} else {
				toFreeSector(sectorsNumbers.get(i));
				sectorsNumbers.remove(i);
				countRemoved++;
				i--;
			}
		}
		
		file.removeSectorsNumbers(countRemoved);
		
		while (data.length() > 0) {
			int additionalSectorNumber = reserveSector();
			file.addSectorNumber(additionalSectorNumber);
			diskPart.writeDataIntoSector(additionalSectorNumber, data);
			data = data.substring(sectorSize < data.length() ? sectorSize : data.length(), data.length());
		}
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
	
	public void relocate(String pathFrom, String nameFrom, String extension, String pathTo, String nameTo) throws Exception {
		copy(pathFrom, nameFrom, extension, pathTo, nameTo, extension);
		remove(pathFrom, nameFrom, extension);
	}
	
	public void copy(String pathFrom, String nameFrom, String extensionFrom, String pathTo, String nameTo, String extensionTo) throws Exception {
		String exFileFromName = nameFrom + (extensionFrom.isEmpty() ? "" : ":" + extensionFrom);
		String fullFileFromName = pathFrom + exFileFromName;
		String exFileToName = nameTo + (extensionTo.isEmpty() ? "" : ":" + extensionTo);
		String fullFileToName = pathTo + exFileToName;
		
		File fileFrom = files.get(fullFileFromName);
		
		create(pathTo, nameTo, extensionTo);
		File fileTo = files.get(fullFileToName);
		
		if (extensionFrom.isEmpty() && extensionTo.isEmpty()) {
			String data = read(fileFrom);
			String[] fileNames = data.split(";");
			
			for (int i = 0; i < fileNames.length; i++) {
				if (!fileNames[i].isEmpty()) {
					File childFile = files.get(fullFileFromName + ">" + fileNames[i]);
					copy(pathFrom + nameFrom + ">", childFile.getName(), childFile.getExtension(), pathTo + nameTo + ">", childFile.getName(), childFile.getExtension());
				}
			}
		} else {
			write(fileTo, read(fileFrom));
		}
	}
	
	public void remove(String path, String name, String extension) throws Exception {
		String exFileName = name + (extension.isEmpty() ? "" : ":" + extension);
		String fullFileName = path + exFileName;
		
		File file = files.get(fullFileName);
		ArrayList<Integer> sectorsNumbers = file.getSectorsNumbers();
		
		if (extension.isEmpty()) {
			String data = read(file);
			String[] names = data.split(";");
			
			for (int i = 0; i < names.length; i++) {
				if (!names[i].isEmpty()) {
					File childFile = files.get(path + name + ">" + names[i]);
					remove(path + name + ">", childFile.getName(), childFile.getExtension());
				}
			}
		}
		
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			toFreeSector(sectorsNumbers.get(i));
		}
		
		files.remove(fullFileName);
		
		if (path.length() > 0) {
			File catalog = files.get(path.substring(0, path.length() - 1));
			String data = read(catalog);
			String[] names = data.split(";");
			
			data = "";
			for (int i = 0; i < names.length; i++) {
				if (!names[i].isEmpty() && !names[i].equals(exFileName)) {
					data += (data.length() > 0 ? ";" : "") + names[i];
				}
			}
			
			write(catalog, data);
		}
	}
	
	private void toFreeSector(int index) {
		if (index >= 0 && index < diskPartSize) {
			map[index] = false;
		}
	}
	
	public ArrayList<Integer> getFileSecotrNumbers(String path, String name, String extension) throws Exception {
		String fullFileName = path + name + (extension.isEmpty() ? "" : ":" + extension);
		File file = files.get(fullFileName);
		return file.getSectorsNumbers();
	}
	
	public boolean[] getMap() {
		return map.clone();
	}
	
	public boolean isFileExist(String path, String name, String extension) {
		String fullFileName = path + name + (extension.isEmpty() ? "" : ":" + extension);
		return files.containsKey(fullFileName);
	}
}
