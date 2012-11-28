/**
* @author: Sean Myers 
*/
package parser.instructions;
import parser.Encoding;

public class RInstruction extends Instruction{
	private int rs, rt, rd, shamt;

	public RInstruction(String optCode) throws InvalidInstructionException{
		this.opt = translateOpName(optCode);
	}
	public RInstruction(String optCode, String rd, String rs, String rt) throws InvalidInstructionException{
		this.opt = translateOpName(optCode); 
		this.rs = Integer.parseInt(rs.substring(2)); 
		this.rt = Integer.parseInt(rt.substring(2)); 
		this.rd = Integer.parseInt(rd.substring(2)); 
	}

	public RInstruction(String optCode, String rd, String rt, int shamt) throws InvalidInstructionException{
		this.opt = translateOpName(optCode); 
		this.rt = Integer.parseInt(rt.substring(2)); 
		this.rd = Integer.parseInt(rd.substring(2)); 
		if(shamt > 32 || shamt < 0){
			throw new InvalidInstructionException("shift amount");
		}
		this.shamt = shamt;

	}
	public int assembleInstruction(){
		int encoding = 0;
		encoding = encodeBits(Encoding.FUNCT.getStartPos(), Encoding.FUNCT.getSize(), opt.getFunct(), encoding);
		encoding = encodeBits(Encoding.SHAMT.getStartPos(), Encoding.SHAMT.getSize(), shamt, encoding);
		encoding = encodeBits(Encoding.RD.getStartPos(), Encoding.RD.getSize(), rd, encoding);
		encoding = encodeBits(Encoding.RS.getStartPos(), Encoding.RS.getSize(), rs, encoding);
		encoding = encodeBits(Encoding.RT.getStartPos(), Encoding.RT.getSize(), rt, encoding);
		encoding = encodeBits(Encoding.OPT.getStartPos(), Encoding.OPT.getSize(), opt.getOpCode(), encoding);
		return encoding;
	}
}

