/**
* @author: Sean Myers
*/


package minijavac.mips.instructions;

public class JInstruction extends Instruction{
	private String immediate;

	public JInstruction(String optCode, String immediate){
		this.opt = translateOpName(optCode); //Opt holds funct info. shamt always is 0
		this.immediate = immediate;
	}

    public String toString(){
        return this.opt.name() + " " + this.immediate;
    }

}
