INPUT_FILE = tests/BubbleSort.java
OUTPUT_FILE = pretty.java

TEST_FILE = semantic/TypeChecking9.java
TEST_FILES = easy-test.java test.java BubbleSort.java \
			 semantic/TypeChecking10.java semantic/TypeChecking11.java semantic/TypeChecking12.java \
			 semantic/TypeChecking9.java semantic/TypeChecking1.java

RUN = java -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac.minijavac

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
	$(RUN) $(INPUT_FILE)

test:            
	javac minijavac/TestSuite.java
	for test in $(TEST_FILES); do \
		$(RUN) tests/$$test > test_output/$$test; \
		java minijavac.TestSuite test_output/$$test test_expected/$$test $$test;\
	done

test_suite:
	$(foreach var, $(TEST_FILES),java -classpath `pwd`/minijavac/:`pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac tests/$(var);)
save:
	java -classpath `pwd`/minijavac/tools/java-cup-11a.jar:`pwd`/ minijavac/minijavac $(INPUT_FILE) > $(OUTPUT_FILE)
clean:
	rm minijavac/*.class  minijavac/tools/*.class minijavac/syntaxtree/*.class minijavac/visitor/*.class minijavac/ir/*.class
