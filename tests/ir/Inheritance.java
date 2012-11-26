class Inheritance {
	public static void main(String[] args) {
		System.out.println(new Foo().Start());
	}
}

class Foo extends Bar {
    public int Start() {
        int a;
        int b;

        a = this.A();
        b = this.B();

        return 0;
    }

    public int A() {
        return 1;
    }
}

class Bar {
    public int B() {
        return 1;
    }
}