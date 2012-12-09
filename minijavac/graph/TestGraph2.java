package minijavac.graph;
import minijavac.ir.*;
import minijavac.graph.*;

public class TestGraph2{

    public static void main(String[] args){
        MethodIR method = new MethodIR("herp", null);
        Quadruple[] quads = new Quadruple[10];
        quads[0] = new Quadruple(InstructionType.PARAM);
        quads[0].arg1 = "a";
        quads[0].operator = "";

        quads[1] = new Quadruple(InstructionType.PARAM);
        quads[1].result = "";
        quads[1].arg1 = "b";
        quads[1].arg2 = "";
        quads[1].operator = "";

        quads[2] = new Quadruple(InstructionType.CALL);
        quads[2].result = "x";
        quads[2].arg1 = "f";
        quads[2].arg2 = "2";
        quads[2].operator = "";


        quads[3] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[3].result = "c";
        quads[3].arg1 = "c";
        quads[3].arg2 = "a";
        quads[3].operator = "+";

        quads[4] = new Quadruple(InstructionType.COND_JUMP);
        quads[4].arg1 = "LOOP";
        quads[4].arg2 = "a";

        quads[5] = new Quadruple(InstructionType.RETURN);
        quads[5].arg1 = "c";

        for(int i = 0; i < 6; i++){
            method.addQuad(quads[i]);
        }

        method.addLabel("LOOP", quads[0]);
        System.out.println("Before~~~~~~~~~~\n\n");
        System.out.println(method);
        RegisterAllocator r = new RegisterAllocator(method);
        r.color();
        System.out.println("\n\nAfter~~~~~~~~\n\n");
        System.out.println(method.toString());
        //System.out.println(l.toStringBlock());


    }
}
