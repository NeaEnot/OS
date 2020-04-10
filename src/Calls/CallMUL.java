package Calls;

public class CallMUL extends Call {
	
	public CallMUL() {
		paramsNumber = 2;
	}

	@Override
	public int invoke(int[] params) {
		return params[0] * params[1];
	}

}
