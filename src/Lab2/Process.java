package Lab2;
import java.util.ArrayList;

public class Process {
	private Timer timer;
	
	private String name;
	private int priority;
	private ArrayList<Thread> threads;
	
	public Process(String name, int priority, Timer timer) {
		this.timer = timer;
		
		this.name = name;
		this.priority = priority;
		
		threads = new ArrayList<Thread>();
		int threadsCount = (int) (Math.random() * 5 + 1);
		for (int i = 0; i < threadsCount; i++) {
			int p = (int) (Math.random() * 5 + 1);
			threads.add(new Thread("" + i, p, timer));
		}
	}
	
	public boolean invoke(int allottedTime) {
		log("Выполняется процесс " + name + " с приоритетом " + priority);
		
		int sumPiority = 0;
		for (int i = 0; i < threads.size(); i++) {
			sumPiority += threads.get(i).getPriority();
		}
		
		int allottedTimeForThread = allottedTime / sumPiority;
		
		for (int i = 0; i < threads.size(); i++) {
			boolean isThreadClosed = threads.get(i).invoke(
									allottedTimeForThread * threads.get(i).getPriority());
			
			if (isThreadClosed) {
				threads.remove(i);
				i--;
			}
		}
		
		if (threads.isEmpty()) {
			log("\tПотоков не осталось");
			return true;
		} else {
			return false;
		}
	}
	
	private void sortThreads() {
		for (int i = 0; i < threads.size() - 1; i++) {
			for (int j = 0; j < threads.size() - 1; j++) {
				if (threads.get(j).getPriority() < threads.get(j + 1).getPriority()) {
					Thread temp = threads.get(j);
					threads.set(j, threads.get(j + 1));
					threads.set(j + 1, temp);
				}
			}
		}
	}
	
	private void log(String message) {
		System.out.println(timer.getTime() + "\t:" + message);
	}
	
	public int getPriority() {
		return priority;
	}
}
