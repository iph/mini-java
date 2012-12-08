package minijavac;

public enum Register{
    STACK("$sp"),
    FRAME("$fp"),
    ZERO("$zero"),
    AT("$at"),
    RETURN("$v0"),
    AUXRETURN("$v1"),
    ARG1("$a0"),
    ARG2("$a1"),
    ARG3("$a2"),
    ARG4("$a3"),
    TEMP1("$t0"),
    TEMP2("$t1"),
    TEMP3("$t2"),
    TEMP4("$t3"),
    TEMP5("$t4"),
    TEMP6("$t5"),
    TEMP7("$t6"),
    TEMP8("$t7"),
    TEMP9("$t8"),
    TEMP10("$t9"),
    SAVE1("$s0"),
    SAVE2("$s1"),
    SAVE3("$s2"),
    SAVE4("$s3"),
    SAVE5("$s4"),
    SAVE6("$s5"),
    SAVE7("$s6"),
    SAVE8("$s7"),
    RESERVED1("$k0"),
    RESERVED2("$k1"),
    GLOBAL("$gp"),
    RETURN_ADDRESS("$ra");

    String regName;
    private Register(String a){
        regName = a;
    }

    public String toString(){
        return regName;
    }
}
