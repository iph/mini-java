package minijavac;

/**
* @author: Sean Myers
*/
import java_cup.runtime.Symbol;
import minijavac.tools.*;
import java.io.*;
import minijavac.visitor.*;
import minijavac.syntaxtree.*;

public class minijavac {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("ERROR: Invalid number of command line arguments.");
			System.err.println("Usage: java main file.java");
			System.exit(1);
		}
		try {
			// Syntax check input file
			MiniJavaParser parser = new MiniJavaParser(new MiniJavaLexer(new FileInputStream(args[0])));
            // Store the root AST node from the parse
            Program program = (Program)parser.parse().value;
            if(parser.hasError){
                return;
            }
            // Build a symbol table from our AST
            SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder(parser.location);
            symbolTableBuilder.visit(program);
            symbolTableBuilder.resolveExtensions();
            System.
            SymbolTable symbolTable = symbolTableBuilder.getSymbolTable();
            // Semantic check the AST
            SemanticChecker semantics = new SemanticChecker(symbolTable, parser.location);
            semantics.visit(program);
            if(semantics.hasError || symbolTableBuilder.hasError){
                return;
            }
            // Convert our AST to intermediate representation
            new IRBuilder(symbolTable).visit(program);
		} catch (IOException e) {
			System.err.println("ERROR: Unable to open file: " + args[0]);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
