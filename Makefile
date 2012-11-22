INPUT_FILE = BubbleSort.java
OUTPUT_FILE = pretty.java

FLEX_FILE = MiniJavaLexer.jflex
GRAMMAR_FILE = MiniJavaParser.cup

all: build

build: build_tools build_class

build_tools:
	java -jar tools/java-cup-11a.jar -package tools -parser MiniJavaParser -interface tools/$(GRAMMAR_FILE)
	mv MiniJavaParser.java tools/
	mv sym.java tools/
	jflex tools/$(FLEX_FILE)
build_class:
	javac -classpath `pwd`/tools/java-cup-11a.jar:`pwd`/ main.java SymbolTable.java SymbolTableBuilder.java IRBuilder.java
run:
	java -classpath `pwd`/tools/:`pwd`/tools/java-cup-11a.jar:`pwd`/ main $(INPUT_FILE)

save:
	java -classpath `pwd`/tools/java-cup-11a.jar:`pwd`/ main $(INPUT_FILE) > $(OUTPUT_FILE)
clean:
	rm *.class  tools/*.class syntaxtree/*.class visitor/*.class
