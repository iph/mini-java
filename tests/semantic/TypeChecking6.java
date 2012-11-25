// Check: If a method is called with the wrong type of argument
class TypeChecking6 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		A a;
		int x;
		boolean y;
		int[] z;

		x = a.add(3, true);
		x = a.add(false, 4);
		x = a.add(3, a);
		x = a.add(a, 4);
		x = a.add(3, z);
		x = a.add(z, 4);
		x = a.add(3, 4);
		x = a.add(z[0], z[0]);
		x = a.add(a.add(1, 2), 4);
		x = a.add(a.add(1, 2), a.add(1, a));

		y = a.isTrue(3);
		y = a.isTrue(a);
		y = a.isTrue(z);
		y = a.isTrue(z[0]);
		y = a.isTrue(true);
		y = a.isTrue(false);
		y = a.isTrue(a.isTrue(false));
		y = a.isTrue(a.isTrue(1));
		
		return 0;
	}
}

class A {
	public int add(int a, int b) {
		return a + b;
	}

	public boolean isTrue(boolean val) {
		return val;
	}
}