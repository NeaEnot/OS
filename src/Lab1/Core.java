package Lab1;
import Calls.Call;
import Calls.CallSUM;
import Calls.CallINC;
import Calls.CallVOID;
import Calls.CallMUL;
import Calls.CallFIBLIKE;

public class Core {
	Stack stack;
	
	public Core(Stack stack) {
		this.stack = stack;
	}
	
	public void Call(int id) {
		Call call;
		
		switch(id) {
		case 0: call = new CallSUM();
			break;
		case 1: call = new CallINC();
			break;
		case 2: call = new CallVOID();
			break;
		case 3: call = new CallMUL();
			break;
		case 4: call = new CallFIBLIKE();
			break;
		default: 
			System.out.println("Неизвестная команда");
			return;
		}
		
		int paramsNumber = call.getParamsNumber();
		int[] params;
		
		if (paramsNumber > 0) {
			params = new int[paramsNumber];
		} else {
			params = null;
		}
		
		for (int i = 0; i < paramsNumber; i++) {
			if (!stack.isEmpty()) {
				params[i] = stack.pop();
			} else {
				System.out.println("Неверное количество параметров в стэке");
				for (int j = i; j >= 0; j++) {
					stack.push(params[i]);
				}
			}
		}
		
		int result = call.invoke(params);
		stack.push(result);
	}
	
	public void getList() {
		System.out.println("CallSUM - 2 параметра");
		System.out.println("CallINC - 1 параметров");
		System.out.println("CallVOID - нет параметров");
		System.out.println("CallMUL - 2 параметра");
		System.out.println("CallFIBLIKE - 3 параметра");
	}
}
