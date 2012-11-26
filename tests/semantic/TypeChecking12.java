// Check: If you try to use this in a static method (main)
class TypeChecking12 {
	public static void main(String[] args) {
		{
			System.out.println(this);
			a = this.someMethod();
		}
	}
}
