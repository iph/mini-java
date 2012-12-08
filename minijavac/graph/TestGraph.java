package minijavac.graph;
import minijavac.ir.*;

public class TestGraph{

    public static void main(String[] args){
        MethodIR method = new MethodIR("herp", null);
        Quadruple[] quads = new Quadruple[11];
        quads[0] = new Quadruple(InstructionType.UNARY_ASSIGN);
        quads[0].result = "x";
        quads[0].arg1 = "0";
        quads[0].operator = "";

        quads[1] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[1].result = "a";
        quads[1].arg1 = "x";
        quads[1].arg2 = "2";
        quads[1].operator = "*";

        quads[2] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[2].result = "b";
        quads[2].arg1 = "a";
        quads[2].arg2 = "5";
        quads[2].operator = "<";

        quads[3] = new Quadruple(InstructionType.COND_JUMP);
        quads[3].arg1 = "L2";
        quads[3].arg2 = "b";

        quads[4] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[4].result = "a";
        quads[4].arg1 = "a";
        quads[4].arg2 = "2";
        quads[4].operator = "+";

        quads[5] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[5].result = "c";
        quads[5].arg1 = "a";
        quads[5].arg2 = "x";
        quads[5].operator = "+";

        quads[6] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[6].result = "b";
        quads[6].arg1 = "x";
        quads[6].arg2 = "10";
        quads[6].operator = "<";

        quads[7] = new Quadruple(InstructionType.COND_JUMP);
        quads[7].arg1 = "L1";
        quads[7].arg2 = "b";

        quads[8] = new Quadruple(InstructionType.COPY);
        quads[8].result = "d";
        quads[8].arg1 = "x";
        quads[8].operator = "";

        quads[9] = new Quadruple(InstructionType.BINARY_ASSIGN);
        quads[9].result = "c";
        quads[9].arg1 = "d";
        quads[9].arg2 = "2";
        quads[9].operator = "+";


        quads[10] = new Quadruple(InstructionType.RETURN);
        quads[10].arg1 = "c";

        for(int i = 0; i < 11; i++){
            method.addQuad(quads[i]);
        }

        method.addLabel("L1", quads[1]);
        method.addLabel("L2", quads[5]);

        RegisterAllocator reg = new RegisterAllocator(method);
        reg.color();
        for(Quadruple quad: method){
            System.out.println(quad.toString());
        }


    }
}
