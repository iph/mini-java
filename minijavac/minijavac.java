package minijavac;

/**
* @author: Sean Myers
*/
import java_cup.runtime.Symbol;
import minijavac.tools.*;
import java.io.*;
import minijavac.visitor.*;
import minijavac.syntaxtree.*;
import minijavac.ir.IR;
import minijavac.mips.*;

public class minijavac {
	public static void main(String[] args) {
		if (args.length < 1 || args.length > 2) {
			System.err.println("ERROR: Invalid number of command line arguments.");
			System.err.println("Usage: java main [-O1] file.java");
			System.exit(1);
		}

            boolean optimize = false;
            if (args.length == 2 && args[1].equals("-O1")) {
                  optimize = true;
            }

		try {
      		// Syntax check input file
      		MiniJavaParser parser = new MiniJavaParser(new MiniJavaLexer(new FileInputStream(args[0])));
                  // Store the root AST node from the parse
                  Program program = (Program)parser.parse().value;
                  if (parser.hasError) {
                      return;
                  }
                  // Build a symbol table from our AST
                  SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder(parser.location);
                  symbolTableBuilder.visit(program);
                  symbolTableBuilder.resolveExtensions();
                  SymbolTable symbolTable = symbolTableBuilder.getSymbolTable();
                  // Semantic check the AST
                  SemanticChecker semantics = new SemanticChecker(symbolTable, parser.location);
                  semantics.visit(program);
                  if (semantics.hasError || symbolTableBuilder.hasError) {
                      return;
                  }
                  // Convert our AST to intermediate representation
                  IRBuilder irBuilder = new IRBuilder(symbolTable);
                  irBuilder.visit(program);
                  IR ir = irBuilder.getIR();

                  // Generate the MIPS code!
                  CodeGenerator codeGen = new MIPSCodeGenerator(ir, symbolTable);
                  Assembly assembly = codeGen.generate(optimize);
                  System.out.println(assembly);
		} catch (IOException e) {
			System.err.println("ERROR: Unable to open file: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
