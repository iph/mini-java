package minijavac.mips;

import java.util.*;

public class MIPSFrameAllocator {
	private int size;
	private HashMap<String, MIPSFrame> frames;

	public MIPSFrameAllocator() {
		frames = new HashMap<String, MIPSFrame>();
	}

	public MIPSFrame getFrame(String klass, String method) {
		return getFrame(klass + "." + method);
	}

	public MIPSFrame getFrame(String canonMethod) {
		MIPSFrame frame = frames.get(canonMethod);
		if (frame == null) {
			frame = new MIPSFrame(canonMethod);
			frames.put(canonMethod, frame);
		}
		return frame;
	}

	public HashMap<String, MIPSFrame> getFrames() {
		return frames;
	}
}