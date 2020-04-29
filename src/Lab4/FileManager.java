package Lab4;

import java.util.Scanner;

public class FileManager {
	private FileSystem fileSystem;
	private FileSystemMonitor monitor;
	
	private String currentCatalog;
	
	public FileManager(FileSystem fileSystem, FileSystemMonitor monitor) {
		this.fileSystem = fileSystem;
		this.monitor = monitor;
		
		currentCatalog = ">";
	}
	
	public int invoke() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print(currentCatalog + " ");
		String cmd = sc.nextLine();
		
		try {
			String[] command = cmd.split(" ");
			
			switch(command[0].toLowerCase()) {
			case "createf":
				if (command.length != 2) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 2");
				}
				createFile(command[1]);
				break;
			case "createc":
				if (command.length != 2) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 2");
				}
				createCatalog(command[1]);
				break;
			case "move":
				if (command.length != 2) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 2");
				}
				move(command[1]);
				break;
			case "read":
				if (command.length != 2) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 2");
				}
				readFile(command[1]);
				break;
			case "write":
				if (command.length < 3) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 3");
				}
				
				String data = "";
				for (int i = 2; i < command.length; i++) {
					data += command[i] + " ";
				}
				
				writeToFile(command[1], data);
				break;
			case "copy":
				if (command.length != 3) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 3");
				}
				copy(command[1], command[2]);
				break;
			case "relocate":
				if (command.length != 3) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 3");
				}
				relocate(command[1], command[2]);
				break;
			case "remove":
				if (command.length != 2) {
					throw new Exception("wrong number of arguments: you enter " + command.length + " arguments, but needs 2");
				}
				remove(command[1]);
				break;
			case "help":
				getHelp();
				break;
			case "close":
				return -1;
			default:
				throw new Exception("unknown command, enter 'help' to get command list");
			}
			
			show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return 0;
	}
	
	private void createFile(String name) throws Exception {
		String[] fullFileNameStruct = getFullFileNameStruct(name, false);
		fileSystem.create(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void createCatalog(String name) throws Exception {
		String[] fullFileNameStruct = getFullFileNameStruct(name, true);
		fileSystem.create(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void move(String dest) throws Exception {
		if (dest.equals("..")) {
			if (currentCatalog.length() <= 1) {
				throw new Exception("you in root catalog");
			}
			
			String[] pathComponents = currentCatalog.split(">");
			
			dest = "";
			for (int i = 0; i < pathComponents.length - 1; i++) {
				dest += pathComponents[i] + ">";
			}
		}
		
		String[] fullFileNameStruct = getFullFileNameStruct(dest, true);
		
		if (!fileSystem.isFileExist(fullFileNameStruct[0], fullFileNameStruct[1], fullFileNameStruct[2])) {
			throw new Exception("catalog is not exist");
		}
		
		currentCatalog = fullFileNameStruct[0] + fullFileNameStruct[1];
		if (!currentCatalog.endsWith(">")) {
			currentCatalog += ">";
		}
		
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void readFile(String name) throws Exception {
		String[] fullFileNameStruct = getFullFileNameStruct(name, false);
		
		if (!fileSystem.isFileExist(fullFileNameStruct[0], fullFileNameStruct[1], fullFileNameStruct[2])) {
			throw new Exception("file is not exist");
		}
		
		System.out.println(fileSystem.read(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]));
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void writeToFile(String name, String data) throws Exception {
		String[] fullFileNameStruct = getFullFileNameStruct(name, false);
		
		if (!fileSystem.isFileExist(fullFileNameStruct[0], fullFileNameStruct[1], fullFileNameStruct[2])) {
			throw new Exception("file is not exist");
		}
		
		fileSystem.write(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2], data);
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void copy(String nameFrom, String nameTo) throws Exception {
		boolean isCatalogFrom = !nameFrom.contains(":");
		boolean isCatalogTo = !nameTo.contains(":");
		
		if (isCatalogFrom != isCatalogTo) {
			throw new Exception("it is can not to copy file into catalog and inversely");
		}
		
		String[] fullFileFromNameStruct = getFullFileNameStruct(nameFrom, isCatalogFrom);
		String[] fullFileToNameStruct = getFullFileNameStruct(nameTo, isCatalogTo);
		
		if (!fileSystem.isFileExist(fullFileFromNameStruct[0], fullFileFromNameStruct[1], fullFileFromNameStruct[2])) {
			throw new Exception("file or catalog is not exist");
		}
		
		fileSystem.copy(fullFileFromNameStruct[0],  fullFileFromNameStruct[1], fullFileFromNameStruct[2],
						fullFileToNameStruct[0],  fullFileToNameStruct[1], fullFileToNameStruct[2]);
		monitor.touchFile(fullFileToNameStruct[0],  fullFileToNameStruct[1], fullFileToNameStruct[2]);
	}
	
	private void relocate(String nameFrom, String nameTo) throws Exception {
		boolean isCatalog = !nameFrom.contains(":");
		
		String[] fullFileFromNameStruct = getFullFileNameStruct(nameFrom, isCatalog);
		String[] fullFileToNameStruct = getFullFileNameStruct(nameTo, isCatalog);
		
		if (!fileSystem.isFileExist(fullFileFromNameStruct[0], fullFileFromNameStruct[1], fullFileFromNameStruct[2])) {
			throw new Exception("file or catalog is not exist");
		}
		
		fileSystem.relocate(fullFileFromNameStruct[0],  fullFileFromNameStruct[1], fullFileFromNameStruct[2],
						fullFileToNameStruct[0],  fullFileToNameStruct[1]);
		monitor.touchFile(fullFileToNameStruct[0],  fullFileToNameStruct[1], fullFileFromNameStruct[2]);
	}
	
	private void remove(String name) throws Exception {
		boolean isCatalog = !name.contains(":");
		String[] fullFileNameStruct = getFullFileNameStruct(name, isCatalog);
		
		if (!fileSystem.isFileExist(fullFileNameStruct[0], fullFileNameStruct[1], fullFileNameStruct[2])) {
			throw new Exception("file or catalog is not exist");
		}
		
		fileSystem.remove(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
		
		fullFileNameStruct = getFullFileNameStruct(currentCatalog, true);
		monitor.touchFile(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]);
	}
	
	private void getHelp() {
		System.out.print(
				"\n" +
				"createf 'name:ext' - create file in current catalog\n" +
				"createf '>path>name:ext' - create file in 'path'\n" +
				"createc 'name' - create catalog in current catalog\n" +
				"createc '>path>name' - create catalog in 'path'\n" +
				"move 'path' - change current catalog to 'path'\n" +
				"move '>fullpath' - change current catalog to 'fullpath'\n" +
				"move '..' - change current catalog to catalog higher in the hierarchy\n" +
				"read 'name:ext' - read file in current catalot\n" +
				"read '>path>name:ext' - read file in 'path'\n" +
				"write 'name:ext' 'data' - write data into file in current catalog\n" +
				"write '>path>name:ext' 'data' - write data into file in 'path'\n" +
				"copy 'nameFrom:ext' 'nameTo:ext' - copy file 'nameFrom.ext' from current catalog into file 'nameTo.ext' in current catalog\n" +
				"copy '>pathFrom>nameFrom:ext' '>pathTo>nameTo:ext' - copy file 'nameFrom:ext' from 'pathFrom' into file 'nameTo:ext' in 'pathTo'\n" +
				"relocate 'nameFrom:ext' 'nameTo:ext' - relocate file 'nameFrom:ext' from current catalog into file 'nameTo:ext' in current catalog\n" +
				"relocate '>pathFrom>nameFrom:ext' '>pathTo>nameTo:ext' - relocate file 'nameFrom:ext' from 'pathFrom' into file 'nameTo:ext' in 'pathTo'\n" +
				"remove 'name:ext' - remove file from current catalot\n" +
				"remove '>path>name:ext' - remove file from 'path'\n" +
				"help - get help\n" +
				"quit - close application\n\n"
				);
	}
	
	private void show() throws Exception {
		String[] fullFileNameStruct = getFullFileNameStruct(currentCatalog, true);
		String[] names = fileSystem.read(fullFileNameStruct[0],  fullFileNameStruct[1], fullFileNameStruct[2]).split(";");

		System.out.println();
		for (int i = 0; i < names.length; i++) {
			if (!names[i].isEmpty()) {
				System.out.println(names[i]);
			}
		}
		System.out.println();
	}
	
	private String[] getFullFileNameStruct(String name, boolean isCatalog) throws Exception {
		if (name.contains(";")) {
			throw new Exception("wrong format: you can not use ';'");
		}
		
		String[] answer = new String[3];
		
		if (name.equals(">")) {
			answer[0] = "";
			answer[1] = "";
			answer[2] = "";
		} else {		
			if (name.charAt(0) != '>') {
				name = currentCatalog + name;
			}
			
			String[] pathComponents = name.split(">");
	
			String path = "";
			for (int i = 0; i < pathComponents.length - 1; i++) {
				path += pathComponents[i] + ">";
			}
	
			String endingName = pathComponents[pathComponents.length - 1];
			String[] endingNameComponents;
			
			if (isCatalog) {
				if (endingName.contains(":")) {
					throw new Exception("wrong format of catalog name: " + endingName + "; catalog name must not contain extension");
				}
				
				endingNameComponents = new String[2];
				endingNameComponents[0] = endingName;
				endingNameComponents[1] = "";
			} else {
				endingNameComponents = endingName.split(":");
				
				if (endingNameComponents.length != 2) {
					throw new Exception("wrong format of file name: " + endingName + "; file name must match the format: 'name:ext'");
				}
			}
			
			answer[0] = path;
			answer[1] = endingNameComponents[0];
			answer[2] = endingNameComponents[1];
		}
		
		return answer;
	}
}
