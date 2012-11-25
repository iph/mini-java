// Check: If a method is called with the wrong number of arguments
class TypeChecking5 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		A a;
		int x;
		boolean y;

		x = a.add();
		x = a.add(3);
		x = a.add(3, 4, 5);
		x = a.add(3, 4);

		x = a.plusZero();
		x = a.plusZero(1, 2);
		x = a.plusZero(1);

		y = a.isTrue();
		y = a.isTrue(true, false);
		y = a.isTrue(false);

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
