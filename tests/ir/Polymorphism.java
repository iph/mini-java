class Polymorphism {
	public static void main(String[] args) {
		System.out.println(new Test().Start());
	}
}

class Test {
    public int Start() {
        Foo var;
        int val;

        var = new Bar();
        val = var.A();
        val = var.B();

        return 0;
    }
}

class Foo {
    public int A() {
        return 0;
    }

    public int B() {
        return 0;
    }
}

class Bar extends Foo {
    public int B() {
        return 1;
    }
}
