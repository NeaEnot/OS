package Lab3;

public class Program {
	private int virtualAdressSpace;
	
	public Program(int virtualAdressSpace) {
		this.virtualAdressSpace = virtualAdressSpace;
	}
	
	public Instruction nextInstruction() {
		int addres = (int) (Math.random() * (virtualAdressSpace - 1));
		
		return new Instruction(addres);
	}
}
