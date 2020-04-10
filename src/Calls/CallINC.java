package Calls;

public class CallINC extends Call {
	
	public CallINC() {
		paramsNumber = 1;
	}

	@Override
	public int invoke(int[] params) {
		return params[0] + 1;
	}

}
