package minijavac.ir;
import java.util.*;

public class IR {
	private ArrayList<MethodIR> methodIRList;

	public IR() {
		methodIRList = new ArrayList<MethodIR>();
	}

	public void addMethodIR(MethodIR ir) {
		methodIRList.add(ir);
	}

	public MethodIR getMethodIR(int index) {
		return methodIRList.get(index);
	}

	public MethodIR getMethodIR(String className, String methodName) {
		for (MethodIR ir : methodIRList) {
			if (ir.getClassName().equals(className) && ir.getMethodName().equals(methodName))
				return ir;
		}
		return null;
	}

	public int size() {
		return methodIRList.size();
	}
}
