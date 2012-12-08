package minijavac.mips;

import java.io.*;
import java.util.*;
import minijavac.Assembly;

public class MIPSAssembly extends Assembly {
	public MIPSAssembly() {
		super();
	}

	public String toString() {
		// output instructions line by line and inject library
		return super.toString() + "\n\n" + getRuntimeLibrary();
	}

	private String getRuntimeLibrary() {
		StringBuffer library = new StringBuffer();
		try {
			FileInputStream in = new FileInputStream("minijavac/mips/library.asm");
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine()) {
				library.append(scanner.nextLine() + "\n");
			}
			in.close();
		} catch (Exception e) {}
		
		return library.toString();
	}
}