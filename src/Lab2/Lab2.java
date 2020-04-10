package Lab2;

public class Lab2 {
	public static void main(String[] args) {
		Timer timer = new Timer();
		Sheduler sheduler = new Sheduler(timer);
		sheduler.start();
	}
}
