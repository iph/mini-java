// Check: If you attempt to use .length on any type other than int []
class TypeChecking9 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		int len;
		int a;
		int[] b;
		boolean c;
		A d;

		len = a.length;
		len = b.length;
		len = c.length;
		len = d.length;
		
		return 0;
	}
}

class A {

}