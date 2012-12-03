package minijavac.mips;

public class MIPSRegisterAllocator {
	private static int tCount = 0;
	private static int aCount = 0;
	private static int sCount = 0;

	public String getParamRegister() {
		if (aCount > 3)
			System.exit(1);
		return "$a" + aCount++;
	}

	public String getTempRegister() {
		if (tCount > 9)
			System.exit(1);
		return "$t" + tCount++;
	}

	public String getSavedTempRegister() {
		if (aCount > 7)
			System.exit(1);
		return "$s" + sCount++;
	}
}