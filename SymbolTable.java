import java.util.*;

public class SymbolTable {
	private HashMap<String, LinkedList<Object>> environment;
    private Stack<String> symbolStack;

	public SymbolTable() {
        environment = new HashMap<String, LinkedList<Object>>();
        symbolStack = new Stack<String>();
        //TODO: Add System.out.println symbols here.
	}

    public String toString(){
        return environment.toString();
    }
    /*
     * starts the symbol stack with a null value as a delimiter for the new scope.
     */
    public void startScope(){
        symbolStack.push(null);
    }

    /*
     * Push any values into the front of the linked list, basically hiding the scope of the previous.
     * This makes it the callers responsibility to check the type of the previous, not the SymbolTable's.
     */
    public void put(String id, Object value){
        if(id == null){
            System.err.println("ID Was null, which is the same as the symbol stack. GET OUT!");
            System.exit(0);
        }
        symbolStack.push(id);

        LinkedList<Object> symbols = environment.get(id);
        if(symbols == null){
            symbols = new LinkedList<Object>();
            environment.put(id, symbols);
        }

        symbols.push(value);
    }

    /*
     * Gets the first object in the linked list, if there is one!
     */
    public Object get(String id){
        LinkedList<Object> symbols = environment.get(id);
        if(symbols == null){
            return null;
        }
        else{
            return symbols.peek();
        }
    }

    /*
     * Undoes all symbols put on in this scope.
     */
    public void endScope(){
        String currentSymbol;
        do{
            currentSymbol = symbolStack.pop();
            environment.get(currentSymbol).pop();
        }
        while(currentSymbol != null);

    }

    public boolean hasId(String id){
        return environment.containsKey(id);
    }


}
