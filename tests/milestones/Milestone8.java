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
	int secondChildObjVal;

	public int Start() {
		int[] arr;
		Object obj;
		ChildObject childObj;
		SecondChildObject secondChildObj;
		int i;

		obj = new Object();
		objVal = obj.Init(9);

		childObj = new ChildObject();
		childObjParentVal = childObj.Init(6);
		childObjVal = childObj.InitChild();

		obj = new ChildObject();
		i = obj.InitChild();
		childObjParentValAgain = obj.getVal();

		secondChildObj = new SecondChildObject();
		secondChildObjVal = secondChildObj.InitChild(3);

		arr = new int[10];
		i = 0;
		while (i < 10) {
			arr[i] = i;
			i = i + 1;
		}

		// 9 + 6 + 6 + 3 + 3 + 9
		return objVal + childObjParentVal + childObjParentValAgain + childObjVal + secondChildObjVal + arr[i];
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
	int val2;

	public int InitChild() {
		int parentVal;

		parentVal = this.Init(6);
		val = 3;

		return val;
	}
}

class SecondChildObject extends Object {
	public int InitChild(int v) {
		int parentVal;

		parentVal = this.Init(100);
		val = v;

		return val;
	}
}