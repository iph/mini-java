INPUT_FILE = tests/BubbleSort.java
OUTPUT_FILE = pretty.java

TEST_FILE = semantic/TypeChecking9.java

FLEX_FILE = MiniJavaLexer.jflex
GRAMMAR_FILE = MiniJavaParser.cup

all: build

build: build_tools build_class

build_tools:
	java -jar minijavac/tools/java-cup-11a.jar -package minijavac -parser MiniJavaParser -interface minijavac/tools/$(GRAMMAR_FILE)
	mv MiniJavaParser.java minijavac/tools/
	mv sym.java minijavac/tools/
	jflex minijavac/tools/$(FLEX_FILE)
build_class:
	javac -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac.java

run:
	java -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac $(INPUT_FILE)

test:
	java -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac tests/$(TEST_FILE)

save:
	java -classpath `pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac $(INPUT_FILE) > $(OUTPUT_FILE)
clean:
	rm minijavac/*.class  minijavac/tools/*.class minijavac/syntaxtree/*.class minijavac/visitor/*.class minijavac/ir/*.class
