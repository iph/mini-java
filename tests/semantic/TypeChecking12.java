// Check: If you try to use this in a static method (main)
class TypeChecking12 {
	public static void main(String[] args) {
		System.out.println(this);
		/* FIXME: this is throwing a parse error when it shouldn't
         * It isn't an error. You are only allowed one statement per
         * main
		{
			System.out.println(this);
			this.someMethod();
		}
		*/
	}
}
