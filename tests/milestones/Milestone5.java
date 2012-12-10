class Test {
    public static void main(String[] args) {
        System.out.println(new Test2().Start());
    }
}

class Test2 {
	public int Start() {
		return this.Recursive(0) + this.Iterative(0);
	}

	public int Recursive(int y) {
		int val;

		if (y < 9 && -1 < y) {
			val = this.Recursive(y + 1);
		} else {
			val = 9;
		}

		return val;
	}

	public int Iterative(int y) {
		int val;

		val = y;
		while (val < 9) {
			val = val + 1;
		}

		return val;
	}
}
