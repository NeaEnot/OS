package Lab1;

public class Lab1 {

	public static void main(String[] args) {
		Stack stack = new Stack();
		Core core = new Core(stack);
		
		stack.push(2);
		stack.push(3);
		stack.print();
		
		core.Call(0);
		stack.print();
		
		core.Call(0);
		
		core.Call(1);
		stack.print();
		
		stack.push(3);
		stack.print();
		
		core.Call(3);
		stack.print();
		
		core.Call(2);
		stack.print();
		
		stack.push(5);
		stack.push(1);
		stack.push(5);
		stack.print();
		
		core.Call(4);
		stack.print();
		
		core.Call(5);
	}

}
