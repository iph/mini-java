package minijavac.mips;

public class MIPSFrame {
	private String canonicalMethodName;
	private int size;

	public MIPSFrame(String method) {
		canonicalMethodName = method;
		// 8 bytes are necessary to store $fp and $ra
		size = 8;
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