class Test {
	public static void main(String[] args) {
		System.out.println(new StupidObject().add(5, 4));
	}
}
class Foo{
    public int A(){
        return 1;
    }
}

class Bar extends Foo{
    public int B(){
        int x;
        /* Works! */
        x = this.A();
        return 1;
    }
}

class StupidObject {
	public int add(int x, int y) {
		if ((x < y) && !true && !(x < y) && !x < y && y < x) {
			b = +1;
			// isn't this stupid?
		} else {
			b = 3 + +1 * +2 + +3 + -5;
			/* yeah, pretty dumb * / /* */
		}

		return x + y;
	}

	public boolean valid() {
		return true;
	}

	public boolean isValid(boolean valid) {
		if (valid) {
			valid = true;
		} else {
			valid = false;
		}
		return valid;
	}
}
