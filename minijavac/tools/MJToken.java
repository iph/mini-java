package minijavac.tools;

public class MJToken {
    public int line, column;
    public Object value;

    public MJToken(int l, int col) {
        line = l;
        column = col;
        value = null;
    }

    public MJToken(int l, int col, Object v) {
        this(l, col);
        value = v;
    }
}