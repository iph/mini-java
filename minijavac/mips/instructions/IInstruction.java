/**
* @author: Sean Myers 
*/
package parser.instructions;

import parser.Encoding;
import parser.Immediate;
import parser.InstructionAssembler;

public class IInstruction extends Instruction{
	private int rs, rt;
	private Immediate immediate;

	public IInstruction(String optCode, String rt, String rs, Immediate immediate) throws InvalidInstructionException{
		this.opt = translateOpName(optCode);

		// Weird swapping first register issue in BEQ and BNE..
		if(optCode.equalsIgnoreCase("BEQ") || optCode.equalsIgnoreCase("BNE")){
			this.rt = Integer.parseInt(rs.substring(2)); 
			this.rs = Integer.parseInt(rt.substring(2)); 			
		}
		else{
			this.rs = Integer.parseInt(rs.substring(2)); 
			this.rt = Integer.parseInt(rt.substring(2)); 			
		}

		this.immediate = immediate;
	}
	
	public IInstruction(String optCode, String rt, Immediate immediate) throws InvalidInstructionException{
		this.opt = translateOpName(optCode);
		this.rs = 0;
		this.rt = Integer.parseInt(rt.substring(2)); 
		this.immediate = immediate;
	}

	public int assembleInstruction(){
		int encoding = 0;
		encoding = encodeBits(Encoding.RS.getStartPos(), Encoding.RS.getSize(), rs, encoding);
		encoding = encodeBits(Encoding.RT.getStartPos(), Encoding.RT.getSize(), rt, encoding);
		encoding = encodeBits(Encoding.OPT.getStartPos(), Encoding.OPT.getSize(), opt.getOpCode(), encoding);
		encoding = encodeBits(Encoding.IMM_16.getStartPos(), Encoding.IMM_16.getSize(), immediate.value(), encoding);
		return encoding;
	}

	public void resolveImmediate(InstructionAssembler ins, int place){
		//All the immediates are slightly different, might pull this logic to the assembler later.
		if(opt.toString().equalsIgnoreCase("LUI")){
			ins.upper(immediate, place);
		}
		else if(opt.toString().equalsIgnoreCase("ORI")){
			ins.lower(immediate, place);
		}
		else if(opt.toString().equalsIgnoreCase("BEQ")){
			ins.offset(immediate, place);
		}
		else if(opt.toString().equalsIgnoreCase("BNE")){
			ins.offset(immediate, place);
		}
	}

}