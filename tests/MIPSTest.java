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

// FIXME: when the class var is d, it thinks
//        it inherited it from Object... is that
//        right? :/
class Subclass extends Object {
    int e;

    public int DiffMethod(int x, int y) {
        e = 1;
        return 0;
    }
}