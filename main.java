/**
* @author: Sean Myers
*/
import java_cup.runtime.Symbol;
import tools.*;
import java.io.*;
import syntaxtree.*;
import visitor.PrettyPrintVisitor;

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
            new PrettyPrintVisitor().visit((Program)parseTree.value);

		} catch (IOException e) {
			System.err.println("ERROR: Unable to open file: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
}
