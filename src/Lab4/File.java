package Lab4;

import java.util.ArrayList;

public class File {
	private String name;
	private String extension;

	private ArrayList<Integer> sectorsNumbers;
	
	public File(String name, String extension) {
		this.name = name;
		this.extension = extension;
		
		sectorsNumbers = new ArrayList<Integer>();
	}
	
	public void addSectorNumber(int number) {
		sectorsNumbers.add(number);
	}
	
	public void removeSectorNumber(int number) {
		for (int i = 0; i < sectorsNumbers.size(); i++) {
			if (sectorsNumbers.get(i) == number) {
				sectorsNumbers.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<Integer> getSectorsNumbers() {
		return (ArrayList<Integer>) sectorsNumbers.clone();
	}
	
	public String getName() {
		return new String(name);
	}
	
	public String getExtension() {
		return new String(extension);
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
