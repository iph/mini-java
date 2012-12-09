package minijavac.mips;

import java.util.*;

public class MIPSFrame {
	private String canonicalMethodName;
    private Map<String, Integer> offset;
	private int size;

	public MIPSFrame(String method) {
		canonicalMethodName = method;
		// 8 bytes are necessary to store $fp and $ra
		size = 8;
        offset = new HashMap<String, Integer>();
	}

    public int get(String varName){
        return offset.get(varName);
    }

    public void allocate(String varName){
        offset.put(varName, size);
        size += 4;


    }
	public void setSize(int bytes) {
		size = bytes;
	}

	public int getSize() {
		return size;
	}

	public String canonicalMethodName() {
		return canonicalMethodName;
	}
}
