package Lab3;

import java.util.TreeSet;

public class PagesTable {
	private int[] table;
	private int[] age;
	private boolean[] isUsed;
	
	private int pagesCountCanOperate;
	private int currentPagesCount = 0;
	
	public PagesTable(int pagesCountAll, int pagesCountCanOperate) {
		this.pagesCountCanOperate = pagesCountCanOperate;
		
		table = new int[pagesCountAll];
		age = new int[pagesCountAll];
		isUsed = new boolean[pagesCountAll];
		
		for (int i = 0; i < pagesCountAll; i++) {
			table[i] = -1;
			age[i] = 0;
			isUsed[i] = false;
		}
	}
	
	public void usingPage(int index) {
		isUsed[index] = true;
	}
	
	public void nextCicle() {
		for (int i = 0; i < isUsed.length; i++) {
			if (table[i] >= 0) {
				age[i] /= 2;
				
				if (isUsed[i]) {
					age[i] += 256;
					isUsed[i] = false;
				}
			}
		}
	}
	
	public int getMap(int index) {
		return table[index];
	}
	
	public int getLessDemandedPageNumber() {
		int lessDemandedPageNumber = -1;
		
		if (!isFullUsed()) {
			for (int i = 0; i < table.length; i++) {
				if (table[i] == -1) {
					lessDemandedPageNumber = i;
					break;
				}
			}
		} else {
			int min = 512;
			int numMin = -1;
			
			for (int i = 0; i < isUsed.length; i++) {
				if (table[i] >= 0) {
					if (age[i] < min) {
						min = age[i];
						numMin = i;
					}
				}
			}
			
			lessDemandedPageNumber = numMin;
		}
		
		return lessDemandedPageNumber;
	}
	
	public void set(int index, int value) {
		table[index] = value;
		age[index] = 0;
		isUsed[index] = false;
		
		if (!isFullUsed()) {
			currentPagesCount++;
		}
	}
	
	public void reset(int index) {
		table[index] = -1;
		age[index] = 0;
		isUsed[index] = false;
	}
	
	public boolean isFullUsed() {
		return currentPagesCount < pagesCountCanOperate ? false : true;
	}
	
	public int getFreePlaceMapped() {
		int answer = -1;
		boolean[] isOccupied = new boolean[pagesCountCanOperate];
		
		for (int i = 0; i < table.length; i++) {
			if (table[i] != -1) {
				isOccupied[table[i]] = true;
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
