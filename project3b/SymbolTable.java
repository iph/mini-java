public class SymbolTable {
	private Environment rootEnvironment;

	public SymbolTable() {
		rootEnvironment = createEnvironment(null);
	}

	public Environment getRootEnvironment() {
		return rootEnvironment;
	}

	public Environment createEnvironment(Environment parent) {
		return new Environment(parent);
	}
}