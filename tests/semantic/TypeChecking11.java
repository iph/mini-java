// Check: If the right hand side of an assignment does not match the type of the left hand side
class TypeChecking11 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	A a;
	B b;
	C c;
	int d;
	boolean e;

	public int Bar() {
		int var1;
		var1 = true;
		a = new B();
		b = new C();
		c = new A();
		d = 1 < 2;
		e = 1;
		return var1;
	}
}

class A {

}

class B extends A {

}

class C {

}