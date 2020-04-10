package Calls;

public class CallSUM extends Call {
	
	public CallSUM() {
		paramsNumber = 2;
	}
	
	@Override
	public int invoke(int[] params) {
		return params[0] + params[1];
	}

}
