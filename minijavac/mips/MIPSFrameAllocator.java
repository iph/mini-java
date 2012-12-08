package minijavac.mips;

public class MIPSFrameAllocator {
	private int size;

	public MIPSFrameAllocator() {
		size = 0;
	}

	public int allocate(int bytes) {
		// 4 bytes are necessary to store $fp
		// at beginning
		int offset = 4 + size;

		size += bytes;

		return offset;
	}

	public int getSize() {
		return size;
	}
}