class TypeChecking5 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {

    public int Moo(int a){

        return 1;
    }
	public int Bar() {
        int a;
		a = this.Moo();
		return 0;
	}
}
