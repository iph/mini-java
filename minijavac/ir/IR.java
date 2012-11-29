package minijavac.ir;
import java.util.*;

public class IR {
	private HashMap<String, MethodIR> methodIR;

	public IR(HashMap<String, MethodIR> mIR) {
		methodIR = mIR;
	}

	public MethodIR getMethod(String methodName) {
		return methodIR.get(methodName);
	}
}
