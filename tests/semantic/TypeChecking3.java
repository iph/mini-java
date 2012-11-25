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

		x = a + 1;
		x = a.add(1, 2) + 1;
		x = Bar + 1;
		x = Foo + 1;
		x = A + 1;
		x = Foo.A() + 1;
		x = Foo.Bar() + 1;

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
