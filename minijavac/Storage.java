package minijavac;

import java.util.*;

public class Storage {
	public final static int HEAP = 0;
	public final static int STACK = 1;
	public final static int GLOBAL = 2;
	public final static int REGISTER = 3;

	private int storageType;
	private int offset;
	private HashMap<String, String> registers;

	public Storage(int type, int off) {
		storageType = type;
		offset = off;
		registers = new HashMap<String, String>();
	}

	public Storage(int type) {
		this(type, 0);
	}

	public Storage() {
		this(STACK);
	}

	/** Returns true if the variable is heap-allocated */
	public boolean onHeap() {
		return storageType == HEAP;
	}

	/** Returns true if the variable is stack-allocated */
	public boolean onStack() {
		return storageType == STACK;
	}

	/** Returns true if the variable is global (in data segment) */
	public boolean isGlobal() {
		return storageType == GLOBAL;
	}

	/** Returns true if the variable lives only in a register */
	public boolean inRegister() {
		return storageType == REGISTER;
	}

	public void setStorageType(int type) {
		storageType = type;
	}

	public void setOffset(int off) {
		offset = off;
	}

	public int getOffset() {
		return offset;
	}

	public void setRegister(String methodName, String register) {
		registers.put(methodName, register);
	}
	
	public String getRegister(String methodName) {
		return registers.get(methodName);
	}
}