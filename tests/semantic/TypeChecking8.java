// Check: If you attempt to use a Boolean operator with non-boolean types (&&)
class TypeChecking8 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		int a;
		int[] b;
		boolean c;
		A d;

		boolean truthiness;

		truthiness = a && a;
		truthiness = b && b;
		truthiness = b[0] && b[0];
		truthiness = a && b;
		truthiness = c && c;
		truthiness = a && c;
		truthiness = d && d;
		truthiness = d && c;
		truthiness = d.getInt() && c;
		truthiness = d.getBoolean() && c;

		return 0;
	}
}

class A {
	public int getInt() {
		return 0;
	}

	public boolean getBoolean() {
		return true;
	}
}