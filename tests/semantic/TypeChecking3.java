// Check: If an operator is applied to a class or method name
class TypeChecking3 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		A a;
		int x;
		boolean y;

		x = a.subtract();
		x = a.A();
		x = this.A();
		x = a.y();
		x = this.y();

		return 0;
	}
}

class B {
	public int plusZero(int a) {
		return a + 0;
	}
}

class A extends B {
	public int add(int a, int b) {
		return a + b;
	}

	public boolean isTrue(boolean val) {
		return val;
	}
}
