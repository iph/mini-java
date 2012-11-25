package minijavac;
import java.io.*;
import java.util.*;
class TestSuite{
    public static void main(String[] args){
        UnitTest test = new UnitTest(args[0], args[1]);
        System.out.println(String.format("Test %-30s...%15s %s", args[2], "", test.run()));
    }
}

class UnitTest{
    File testOutput, testComparison;
    String testOutputName, testComparisonName;

    public UnitTest(String tOutput, String tComparison){
        testOutput = new File(tOutput);
        testComparison = new File(tComparison);
        testOutputName = tOutput;
        testComparisonName = tComparison;
    }
    public String run(){
        Scanner a, b;
        if(!testOutput.exists()){
            return "Error: could not open file" + testOutputName;
        }
        if(!testComparison.exists()){
            return "Error: could not open file "+ testComparisonName;
        }

        try{
            a = new Scanner(testOutput);
            b = new Scanner(testComparison);

        }
        catch(IOException e){
            return "Error: File operation fucked up";
        }
        int line = 0;
        while(a.hasNext()){
            if(!b.hasNext()){
                return "Failed: Comparison file has too little lines to compare to";
            }
            if(!a.nextLine().equalsIgnoreCase(b.nextLine())){
                return "Failed: Wrong at line " + line;
            }
        }
        if(b.hasNext()){
            return "Failed: Original file didn't have enough lines";
        }

        return  "Success";
    }
}
