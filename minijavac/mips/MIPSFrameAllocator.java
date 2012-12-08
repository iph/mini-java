package minijavac.mips;

import java.util.*;

public class MIPSFrameAllocator {
	private int size;
	private HashMap<String, MIPSFrame> frames;

	public MIPSFrameAllocator() {
		frames = new HashMap<String, MIPSFrame>();
	}

	public int allocate(String method, int bytes) {
		MIPSFrame frame = frames.get(method);
		if (frame == null) {
			frame = new MIPSFrame(method);
			frames.put(method, frame);
		}

		int offset = frame.getSize();

		frame.setSize(offset + bytes);

		return offset;
	}

	public HashMap<String, MIPSFrame> getFrames() {
		return frames;
	}
}