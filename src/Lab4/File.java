package Lab4;

import java.util.ArrayList;

public class File {
	private String name;
	private String extension;
	
	private Node node;
	
	public File(String name, String extension) {
		this.name = name;
		this.extension = extension;
		
		node = new Node();
	}
	
	public void addSectorNumber(int number) {
		node.addSectorNumber(number);
	}
	
	public void removeSectorsNumbers(int count) {
		node.removeSectorsNumbers(count);
	}
	
	public ArrayList<Integer> getSectorsNumbers() {
		return node.getSectorsNumbers();
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
