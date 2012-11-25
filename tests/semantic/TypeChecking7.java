// Check: If you attempt to use an arithmetic or comparison operator (<, +, -, *) with non-integer operands
class TypeChecking7 {
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
		int val;

		truthiness = a < a;
		truthiness = a < b;
		truthiness = a < b[0];
		truthiness = a < c;
		truthiness = a < d;
		truthiness = a < d.getInt();
		truthiness = a < d.getBoolean();
		
		val = a + a;
		val = a + b;
		val = a + b[0];
		val = a + c;
		val = a + d;
		val = a + d.getInt();
		val = b + b;
		val = c + c;
		val = d + d;
		val = d.getInt() + d.getInt();
		val = d.getBoolean() + a;

		val = a - a;
		val = a - b;
		val = a - b[0];
		val = a - c;
		val = a - d;
		val = a - d.getInt();
		val = b - b;
		val = c - c;
		val = d - d;
		val = d.getInt() - d.getInt();
		val = d.getBoolean() - a;

		val = a * a;
		val = a * b;
		val = a * b[0];
		val = a * c;
		val = a * d;
		val = a * d.getInt();
		val = b * b;
		val = c * c;
		val = d * d;
		val = d.getInt() * d.getInt();
		val = d.getBoolean() * a;

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