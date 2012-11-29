INPUT_FILE = tests/TreeVisitor.java
OUTPUT_FILE = TreeVisitorIR.txt

TEST_FILES = easy-test.java test.java BubbleSort.java \
			 ir/Inheritance.java \
			 semantic/TypeChecking12.java semantic/TypeChecking11.java semantic/TypeChecking10.java \
			 semantic/TypeChecking9.java semantic/TypeChecking8.java semantic/TypeChecking7.java \
			 semantic/TypeChecking6.java semantic/TypeChecking5.java semantic/TypeChecking4.java \
			 semantic/TypeChecking3.java semantic/TypeChecking2.java semantic/TypeChecking1.java \
			 ir/milestone1.java

RUN = java -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac

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
	javac -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac.java minijavac/ClassAttribute.java

run:
	$(RUN) $(INPUT_FILE)

test:            
	javac minijavac/TestSuite.java
	for test in $(TEST_FILES); do \
		$(RUN) tests/$$test > tests/actual_output/$$test; \
		java minijavac.TestSuite tests/actual_output/$$test tests/expected_output/$$test $$test;\
	done

save:
	java -classpath `pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac $(INPUT_FILE) > $(OUTPUT_FILE)

clean:
	rm minijavac/*.class  minijavac/tools/*.class minijavac/syntaxtree/*.class minijavac/visitor/*.class minijavac/ir/*.class
