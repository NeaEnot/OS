package Calls;

public abstract class Call {
	protected int paramsNumber;
	
	abstract public int invoke(int[] params);
	
	public int getParamsNumber() {
		return paramsNumber;
	}
}
