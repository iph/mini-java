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
        b = 1;
        c = 2;
        a = b + c;
        d = 4;
    	return 0;
    }
}

class Subclass extends Object {
    int d;

    public int DiffMethod(int x, int y) {
        d = 1;
        while (true) {
            if (true)
                d = 2;
            else {
                if (true) 
                if ((!true) && 
                    (!true) )
                    d = 4;
                else 
                    d = 5;
                else d = 6;
            }
        }
        return 0;
    }
}