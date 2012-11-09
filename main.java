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
			Parser parser_obj = new Parser(new JavaLex(new FileInputStream(args[0])));
            parser_obj.parse();
            new PrettyPrintVisitor().visit(parser_obj.p);

		} catch (IOException e) {
			System.err.println("ERROR: Unable to open file: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
}
