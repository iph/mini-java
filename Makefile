INPUT_FILE = test.java
OUTPUT_FILE = pretty.java

FLEX_FILE = mini_java.flex
GRAMMAR_FILE = grammar_mini_java.cup

all: build run

build: build_tools build_class

build_tools:
	java -jar tools/java-cup-11a.jar -expect 4 -package tools -parser Parser -interface tools/$(GRAMMAR_FILE)
	mv Parser.java tools/
	mv sym.java tools/
	jflex tools/$(FLEX_FILE)
build_class:
	javac -classpath `pwd`/tools/java-cup-11a.jar:`pwd`/ main.java
run:
	java -classpath `pwd`/tools/:`pwd`/tools/java-cup-11a.jar:`pwd`/ main $(INPUT_FILE)

save:
	java -classpath `pwd`/tools/java-cup-11a.jar:`pwd`/ main $(INPUT_FILE) > $(OUTPUT_FILE)
clean:
	rm *.class  tools/*.class syntaxtree/*.class visitor/*.class
