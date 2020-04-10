package Calls;

public class CallFIBLIKE extends Call {
	
	public CallFIBLIKE() {
		paramsNumber = 3;
	}

	@Override
	public int invoke(int[] params) {
		int old = params[0];
		int current = params[1];
		int steps = params[2];
		
		if (steps == 0) {
			return old;
		}
		
		if (steps == 1) {
			return current;
		}
		
		if (steps < 0) {
			return 0;
		}
		
		for (int i = 2; i <= steps; i++) {
			int vedro = current;
			current = current + old;
			old = vedro;
		}
		
		return current;
	}

}
