class MIPSTest {
    public static void main(String[] args) {
	   System.out.println(new Object().Method(1, 2));
    }
}

class Object {
    int a;
    int b;
    int c;
    int d;

    public int Method(int x, int y) {
        int d;
        b = 1;
        c = 2;
        a = b + c;
        d = 4;
    	return 0;
    }
}

// FIXME: when the class var is d, it not only thinks
//        it inherited it from Object, it thinks it
//        has access to it. is that right? :/
// EDIT:  I fixed this by changing how getInMyScope works so
//        that only methods of parent classes are added.
//        Is that the desired behavior, or are we actually
//        supposed to inherit variables publicly?
class Subclass extends Object {
    int d;

    public int DiffMethod(int x, int y) {
        d = 1;
        return 0;
    }
}