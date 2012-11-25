class TypeChecking10 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {

    public int Moo(){

        return 1;
    }
	public int Bar() {
		Foo a;
		a = this;
		a = Moo;
		return 0;
	}
}
