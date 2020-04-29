package Lab4;

import java.util.ArrayList;

public class Node {
	private int[] sectorsNumbers;
	private int count;
	private Node nextNode;
	
	public Node() {
		sectorsNumbers = new int[8];
		count = 0;
	}
	
	public int size() {
		return count + (nextNode != null ? nextNode.size() : 0);
	}
	
	public void addSectorNumber(int number) {
		if (count < sectorsNumbers.length) {
			sectorsNumbers[count] = number;
			count++;
		} else {
			 if (nextNode == null) {
				 nextNode = new Node();
			 }
			 
			 nextNode.addSectorNumber(number);
		}
	}
	
	public void removeSectorsNumbers(int count) {
		if (nextNode != null) {
			if (nextNode.size() >= count) {
				nextNode.removeSectorsNumbers(count);
				count = 0;
			} else {
				int dif = count - nextNode.size();
				nextNode.removeSectorsNumbers(dif);
				count -= dif;
			}
			
			if (nextNode.size() == 0) {
				nextNode = null;
			}
		}
		
		this.count = this.count - count;
		if (this.count < 0) {
			this.count = 0;
		}
	}
	
	public ArrayList<Integer> getSectorsNumbers() {
		ArrayList<Integer> answer = new ArrayList<Integer>();
		
		for (int i = 0; i < count; i++) {
			answer.add(sectorsNumbers[i]);
		}
		
		if (nextNode != null) {
			answer.addAll(nextNode.getSectorsNumbers());
		}
		
		return answer;
	}
}
