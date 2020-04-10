package Lab3;

public class Lab3 {

	public static void main(String[] args) {
		int programSize = 100;
		int pageSize = 10;
		Memory HDD = new Memory(1000, pageSize, false);
		Memory RAM = new Memory(50, pageSize, true);
		
		MemoryManager memoryManager = new MemoryManager(HDD, RAM, pageSize, programSize);
		
		memoryManager.invoke(20);
	}

}
