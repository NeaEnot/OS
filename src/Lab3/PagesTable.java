package Lab3;

public class PagesTable {
	private PagesTableRecord[] table;
	
	private int pagesCountCanOperate;
	private int currentPagesCount = 0;
	
	public PagesTable(int pagesCountAll, int pagesCountCanOperate) {
		this.pagesCountCanOperate = pagesCountCanOperate;
		
		table = new PagesTableRecord[pagesCountAll];
		for (int i = 0; i < pagesCountAll; i++) {
			table[i] = new PagesTableRecord();
		}
	}
	
	public void usingPage(int index) {
		table[index].setIsUsed(true);
	}
	
	public void nextCicle() {
		for (int i = 0; i < table.length; i++) {
			if (table[i].getPageNumber() >= 0) {
				table[i].setAge(table[i].getAge() / 2);
				
				if (table[i].getIsUsed()) {
					table[i].setAge(table[i].getAge() + 256);
					table[i].setIsUsed(false);
				}
			}
		}
	}
	
	public int getMap(int index) {
		return table[index].getPageNumber();
	}
	
	public int getLessDemandedPageNumber() {
		int lessDemandedPageNumber = -1;
		
		if (!isFullUsed()) {
			for (int i = 0; i < table.length; i++) {
				if (table[i].getPageNumber() == -1) {
					lessDemandedPageNumber = i;
					break;
				}
			}
		} else {
			int min = 512;
			int numMin = -1;
			
			for (int i = 0; i < table.length; i++) {
				if (table[i].getPageNumber() >= 0) {
					if (table[i].getAge() < min) {
						min = table[i].getAge();
						numMin = i;
					}
				}
			}
			
			lessDemandedPageNumber = numMin;
		}
		
		return lessDemandedPageNumber;
	}
	
	public void set(int index, int value) {
		table[index].setPageNumber(value);
		table[index].setAge(0);
		table[index].setIsUsed(false);
		
		if (!isFullUsed()) {
			currentPagesCount++;
		}
	}
	
	public void reset(int index) {
		table[index].setPageNumber(-1);
		table[index].setAge(0);
		table[index].setIsUsed(false);
	}
	
	public boolean isFullUsed() {
		return currentPagesCount < pagesCountCanOperate ? false : true;
	}
	
	public int getFreePlaceMapped() {
		int answer = -1;
		boolean[] isOccupied = new boolean[pagesCountCanOperate];
		
		for (int i = 0; i < table.length; i++) {
			if (table[i].getPageNumber() != -1) {
				isOccupied[table[i].getPageNumber()] = true;
			}
		}
		
		for (int i = 0; i < isOccupied.length; i++) {
			if (!isOccupied[i]) {
				answer = i;
				break;
			}
		}
		
		return answer;
	}
}
