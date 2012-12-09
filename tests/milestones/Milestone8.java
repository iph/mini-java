class Test {
    public static void main(String[] args) {
        System.out.println(new Test2().Start());
    }
}

class Test2 {
	int objVal;
	int childObjParentVal;
	int childObjParentValAgain;
	int childObjVal;

	public int Start() {
		int[] arr;
		Object obj;
		ChildObject childObj;
		int i;

		obj = new Object();
		objVal = obj.Init(9);

		childObj = new ChildObject();
		childObjParentVal = childObj.Init(6);
		childObjVal = childObj.InitChild();

		obj = new ChildObject();
		i = obj.InitChild();
		childObjParentValAgain = obj.getVal();

		arr = new int[10];
		i = 0;
		while (i < 10) {
			arr[i] = i;
			i = i + 1;
		}

		// 9 + 6 + 6 + 3 + 9
		return objVal + childObjParentVal + childObjParentValAgain + childObjVal + arr[i];
	}
}

class Object {
	int val;

	public int Init(int val) {
		int v;

		v = val;
		return this.val(v);
	}

	public int val(int v) {
		val = v;
		return val;
	}

	public int getVal() {
		return val;
	}
}

class ChildObject extends Object {
	int val;

	public int InitChild() {
		int parentVal;

		parentVal = this.Init(6);
		val = 3;

		return val;
	}
}