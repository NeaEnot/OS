package Lab1;

public class Stack {
	private int[] stack = new int[0];
	
	public int pop() {
		int answer = stack[stack.length - 1];
		
		int[] vedro = stack;
		stack = new int[stack.length - 1];
		
		for(int i = 0; i < stack.length; i++) {
			stack[i] = vedro[i];
		}
		
		return answer;
	}
	
	public void push(int n) {
		int[] vedro = stack;
		stack = new int[stack.length + 1];
		
		for(int i = 0; i < stack.length - 1; i++) {
			stack[i] = vedro[i];
		}
		
		stack[stack.length - 1] = n;
	}
	
	public boolean isEmpty() {
		return stack.length == 0;
	}
	
	public void print() {
		for (int i = 0; i < stack.length; i++) {
			System.out.print(stack[i] + "; ");
		}
		System.out.println();
	}
}
