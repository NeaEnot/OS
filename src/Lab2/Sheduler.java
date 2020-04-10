package Lab2;
import java.util.ArrayList;

public class Sheduler {
	private Timer timer;
	
	private ArrayList<Process> processes;
	
	public Sheduler(Timer timer) {
		this.timer = timer;
		
		int processesCount = (int) (Math.random() * 4 + 3);
		processes = new ArrayList<Process>();
		for (int i = 0; i < processesCount; i++) {
			int p = (int) (Math.random() * 5);
			processes.add(new Process("" + i, p, timer));
		}
	}
	
	public void start() {
		sortProcesses();
		
		while (!processes.isEmpty()) {
			int p = processes.get(0).getPriority();
			int i = 0;
			
			while (i < processes.size() && processes.get(i).getPriority() == p) {
				int allottedTime = 500;
				
				boolean isProcessClosed = processes.get(i).invoke(allottedTime);
				
				if (isProcessClosed) {
					processes.remove(i);
					i--;
				}
				
				i++;
			}
		}
		
		/*int allottedTime = 500;
			
			boolean isProcessClosed = processes.get(0).invoke(allottedTime);
			
			if (isProcessClosed) {
				processes.remove(0);
			}*/
	}
	
	private void sortProcesses() {
		for (int i = 0; i < processes.size() - 1; i++) {
			for (int j = 0; j < processes.size() - 1; j++) {
				if (processes.get(j).getPriority() < processes.get(j + 1).getPriority()) {
					Process temp = processes.get(j);
					processes.set(j, processes.get(j + 1));
					processes.set(j + 1, temp);
				}
			}
		}
	}
	
	private void log(String message) {
		System.out.println(timer.getTime() + "\t:\t" + message);
	}
}
