package minijavac.mips;

import java.util.*;
import java.io.*;
import minijavac.ir.*;
import minijavac.*;

public class MIPSCodeGenerator extends CodeGenerator {
	private MIPSObjectTransformer objectTransformer;
	private MIPSRegisterAllocator registerAllocator;
	private MIPSFrameAllocator frameAllocator;
	private MIPSTranslator translator;

	public MIPSCodeGenerator(IR ir, SymbolTable symbolTable) {
		super(ir, symbolTable);
		objectTransformer = new MIPSObjectTransformer(symbolTable);
		frameAllocator = new MIPSFrameAllocator();
		registerAllocator = new MIPSRegisterAllocator(symbolTable, frameAllocator);
		translator = new MIPSTranslator(symbolTable, registerAllocator, frameAllocator);
	}

	public Assembly generate() {
		System.out.println("BEFORE");
		System.out.println(ir);
		// add offsets and other stuff to class vars
		storeStorageData();

		// update IR to reflect loading and storing class vars
		objectTransformer.transformIR(ir);

		System.out.println("AFTER");
		System.out.println(ir);

		registerAllocator.allocate(ir);

		return translator.translate(ir);
	}

	// TODO
	// iterate through all classes in symbol table
	// updating their variable attributes (accounting for
	// the data storage oddness that comes with inheritance)
	// as well as iterating through their methods and updating
	// the methods' variable attributes
	public void storeStorageData() {
		HashMap<String, LinkedList<Object>> environment = symbolTable.getEnvironment();
		for (Map.Entry<String, LinkedList<Object>> entry : environment.entrySet()) {
			Attribute attr = (Attribute)(entry.getValue().get(0));
			if (!(attr instanceof ClassAttribute)) {
				continue;
			}

			ClassAttribute cls = (ClassAttribute)attr;
			storeClassSize(cls);
		}
	}

	/** Calculates the size of a class, as well
	    as the offsets necessary for each class var,
	    and stores them in the appropriate attribute.

	    Note: Will recursively ascend the class hierarchy.
	 */
	public int storeClassSize(ClassAttribute cls) {
		int parentSize = 0;
		if (cls.getParentClass() != null) {
			parentSize = storeClassSize(cls.getParentClass());
		}

		// Have we already calculated this class's size?
		if (cls.getSize() != -1) {
			return cls.getSize();
		}

		// we're on a 'root' class, so we can update var offsets and calculate size
		int sizeInBytes = 0;
		int lastOffsetInBytes = parentSize;
		// we want our offset to start on a multiple of 4
		while (lastOffsetInBytes % 4 != 0) {
			lastOffsetInBytes++;
			sizeInBytes++;
		}
		for (Map.Entry<String, VariableAttribute> var : cls.getVariables().entrySet()) {
			Storage varData = var.getValue().getStorage();
			varData.setStorageType(Storage.HEAP);
			varData.setOffset(lastOffsetInBytes);
			// every type is conveniently WORD-sized
			lastOffsetInBytes += 4;
			sizeInBytes += 4;
		}

		cls.setSize(sizeInBytes);

		return sizeInBytes;
	}
}