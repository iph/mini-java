/**
* @author: Sean Myers
*/
import java_cup.runtime.Symbol;
import tools.*;
import java.io.*;
import syntaxtree.*;

public class main {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("ERROR: Invalid number of command line arguments.");
			System.err.println("Usage: java main file.java");
			System.exit(1);
		}
		try {
			MiniJavaParser parser = new MiniJavaParser(new MiniJavaLexer(new FileInputStream(args[0])));
            Symbol parseTree = parser.parse();

            new SymbolTableBuilder(parser.location).visit((Program)parseTree.value);
            new IRBuilder().visit((Program)parseTree.value);
		} catch (IOException e) {
			System.err.println("ERROR: Unable to open file: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
}
