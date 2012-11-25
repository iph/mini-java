// Check: If the condition of an if or while statement does not evaluate to a boolean
class TypeChecking10 {
	public static void main(String[] args) {
		System.out.println(new Foo().Bar());
	}
}

class Foo {
	public int Bar() {
		int a;
		a = this.testIfs();
		a = this.testWhiles();
		return 0;
	}

	public int testIfs() {
		int var1;
		int var2;

		var1 = 1;
		var2 = 2;

		if (var1) {

		} else {
			if (var2) {

			} else {

			}
		}

		if (var1 + var2) {

		} else {

		}

		if (!var1) {

		} else {

		}

		if (var1 < var2) {

		} else {

		}

		if (1) {

		} else {

		}

		if (False < 1) {

		} else {

		}

		return 0;
	}

	public int testWhiles() {
		int var1;
		int var2;

		var1 = 1;
		var2 = 2;

		while (var1) {

		}

		while (var1 + var2) {

		}

		while (!var1) {

		}

		while (var1 < var2) {

		}

		while (1) {

		}

		while (False < 1) {

		}

		return 0;
	}
}
