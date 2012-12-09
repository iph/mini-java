class Test {
    public static void main(String[] args) {
        System.out.println(new Test2().Start());
    }
}

class Test2 {
	public int Start() {
		int small;
		int big;

		small = this.SmallFunction(6, 6, 6);
		big = this.BigFunction(2, 2, 4, 4, 6);
		return small + big;
	}

	public int SmallFunction(int a, int b, int c) {
		int formalSum;
		int localSum;

		formalSum = a + b + c;

		a = 3;
		b = 3;
		c = 3;

		localSum = a + b + c;

		return formalSum - localSum;
	}

	public int BigFunction(int a, int b, int c, int d, int e) {
		int formalSum;
		int localSum;

		formalSum = a + b + c + d + e;

		a = 1;
		b = 1;
		c = 2;
		d = 2;
		e = 3;

		localSum = a + b + c + d + e;

		return formalSum - localSum;
	}
}
