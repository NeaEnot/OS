package Lab3;

public class PagesTableRecord {
	private int pageNumber;
	private int age;
	private boolean isUsed;
	
	public PagesTableRecord() {
		pageNumber = -1;
		age = 0;
		isUsed = false;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getAge() {
		return age;
	}
	
	public boolean getIsUsed() {
		return isUsed;
	}
	
	public void setPageNumber(int newPageNumber) {
		pageNumber = newPageNumber;
	}
	
	public void setAge(int newAge) {
		age = newAge;
	}
	
	public void setIsUsed(boolean newIsUsed) {
		isUsed = newIsUsed;
	}
}
