/**
* @author: Sean Myers
*/
package minijavac.mips.instructions;
import minijavac.mips.Operation;

public abstract class Instruction{
	protected Operation opt;


	/* Tries to translate the string name into an Operation enum. Throws InvalidInstructionException if
	it can't find the enum. */
	protected Operation translateOpName(String opt){
		for(Operation i: Operation.values()){
			if (opt.equalsIgnoreCase(i.toString())){
				return i;
			}
		}
        return null;
	}
}


