class TypeChecking4 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
        int b;
        int a;
        a = Foo.notReal();
		return 0;
	}
}
