import java.util.Hashtable;

public class Environment {
	public Environment parent;
	private Hashtable<String,Object> symbolTable;

	public Environment(Environment p) {
		parent = p;
		symbolTable = new Hashtable<String,Object>();
	}

	public Object get(String identifier);
	public void put(String identifier, Object value);
	public Environment lookup(String identifier);
}
