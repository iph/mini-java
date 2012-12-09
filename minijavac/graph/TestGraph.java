package minijavac.graph;
import minijavac.ir.*;

public class TestGraph{

    public static void main(String[] args){
        MethodIR method = new MethodIR("herp", "derp", null);
        Quadruple[] quads = new Quadruple[11];

        quads[0] = new Quadruple(InstructionType.COPY);
        quads[0].result = "t3";
        quads[0].arg1 = "0";
        quads[0].operator = "";

        quads[1] = new Quadruple(InstructionType.PARAM);
        quads[1].result = "";
        quads[1].arg1 = "t3";
        quads[1].arg2 = "";
        quads[1].operator = "";

        quads[2] = new Quadruple(InstructionType.CALL);
        quads[2].result = "t0";
        quads[2].arg1 = "_new_object";
        quads[2].arg2 = "1";
        quads[2].operator = "";

        quads[3] = new Quadruple(InstructionType.COPY);
        quads[3].result = "t1";
        quads[3].arg1 = "9";

        quads[4] = new Quadruple(InstructionType.PARAM);
        quads[4].result = "";
        quads[4].arg1 = "t0";
        quads[4].arg2 = "";
        quads[4].operator = "";

        quads[5] = new Quadruple(InstructionType.PARAM);
        quads[5].result = "";
        quads[5].arg1 = "t1";
        quads[5].arg2 = "";
        quads[5].operator = "";

        quads[6] = new Quadruple(InstructionType.CALL);
        quads[6].result = "t2";
        quads[6].arg1 = "Test2.Start";
        quads[6].arg2 = "2";
        quads[6].operator = "";

        quads[7] = new Quadruple(InstructionType.PARAM);
        quads[7].arg1 = "t2";
        quads[7].arg2 = "";

        quads[8] = new Quadruple(InstructionType.CALL);
        quads[8].result = "p";
        quads[8].arg1 = "System.out.println";
        quads[8].operator = "1";

        for(int i = 0; i < 9; i++){
            method.addQuad(quads[i]);
        }

        RegisterAllocator reg = new RegisterAllocator(method);
        reg.color();
        System.out.println(method);


    }
}
