package minijavac.mips;

import java.util.*;

public class MIPSFrameAllocator {
	private int size;
	private HashMap<String, MIPSFrame> frames;

	public MIPSFrameAllocator() {
		frames = new HashMap<String, MIPSFrame>();
	}

	public int allocate(String klass, String method, int bytes) {
		return allocate(klass + "." + method, bytes);
	}

	public int allocate(String canonMethod, int bytes) {
		MIPSFrame frame = frames.get(canonMethod);
		if (frame == null) {
			frame = new MIPSFrame(canonMethod);
			frames.put(canonMethod, frame);
		}

		int offset = frame.getSize();

		frame.setSize(offset + bytes);

		return offset;
	}

	public MIPSFrame getFrame(String klass, String method) {
		return getFrame(klass + "." + method);
	}

	public MIPSFrame getFrame(String canonMethod) {
		return frames.get(canonMethod);
	}

	public HashMap<String, MIPSFrame> getFrames() {
		return frames;
	}
}