class Test {
    public static void main(String[] args) {
        System.out.println(new Test2().Start());
    }
}

class Test2 {
	public int Start() {
		return this.base2pow(16);
	}

	public int base2pow(int pow) {
		int result;
		int i;

		if (pow < 1) {
			result = 1;
		} else {
			result = 2;
			i = 1;
			while (i < pow) {
				result = result * 2;
				i = i + 1;
			}
		}
		
		return result;
	}
}
