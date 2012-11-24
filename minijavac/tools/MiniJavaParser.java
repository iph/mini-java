package minijavac.tools;

import java.util.*;
import java_cup.runtime.Symbol;
import minijavac.syntaxtree.*;
import minijavac.visitor.*;

public class MiniJavaParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public MiniJavaParser() {super();}

  /** Constructor which sets the default scanner. */
  public MiniJavaParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public MiniJavaParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\076\000\002\002\004\000\002\002\004\000\002\003" +
    "\023\000\002\004\004\000\002\004\002\000\002\005\012" +
    "\000\002\005\010\000\002\005\004\000\002\006\004\000" +
    "\002\006\002\000\002\007\004\000\002\007\002\000\002" +
    "\010\004\000\002\010\002\000\002\011\005\000\002\011" +
    "\005\000\002\012\015\000\002\012\004\000\002\013\005" +
    "\000\002\013\004\000\002\014\005\000\002\014\002\000" +
    "\002\015\005\000\002\016\004\000\002\016\002\000\002" +
    "\017\005\000\002\017\003\000\002\017\003\000\002\017" +
    "\003\000\002\020\005\000\002\020\011\000\002\020\007" +
    "\000\002\020\007\000\002\020\006\000\002\020\011\000" +
    "\002\020\004\000\002\021\005\000\002\021\005\000\002" +
    "\021\005\000\002\021\005\000\002\021\005\000\002\021" +
    "\006\000\002\021\005\000\002\021\010\000\002\021\007" +
    "\000\002\021\003\000\002\021\003\000\002\021\003\000" +
    "\002\021\003\000\002\021\007\000\002\021\006\000\002" +
    "\021\004\000\002\021\005\000\002\022\004\000\002\022" +
    "\002\000\002\023\004\000\002\024\004\000\002\024\002" +
    "\000\002\025\003\000\002\026\003\000\002\026\004\000" +
    "\002\026\004" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\236\000\004\024\005\001\002\000\004\002\240\001" +
    "\002\000\004\047\014\001\002\000\010\002\ufffd\003\ufffd" +
    "\024\ufffd\001\002\000\010\002\000\003\010\024\012\001" +
    "\002\000\004\021\217\001\002\000\010\002\ufffe\003\ufffe" +
    "\024\ufffe\001\002\000\004\047\014\001\002\000\006\020" +
    "\016\031\015\001\002\000\024\003\uffc7\005\uffc7\006\uffc7" +
    "\007\uffc7\016\uffc7\017\uffc7\020\uffc7\022\uffc7\031\uffc7\001" +
    "\002\000\004\047\014\001\002\000\016\003\ufff6\021\ufff6" +
    "\025\ufff6\043\ufff6\044\ufff6\047\ufff6\001\002\000\016\003" +
    "\ufff4\021\ufff4\025\ufff4\043\025\044\022\047\023\001\002" +
    "\000\010\003\035\021\037\025\036\001\002\000\006\003" +
    "\031\047\014\001\002\000\010\003\uffe7\022\026\047\uffe7" +
    "\001\002\000\006\003\uffe5\047\uffe5\001\002\000\030\003" +
    "\ufff7\020\ufff7\021\ufff7\025\ufff7\033\ufff7\034\ufff7\036\ufff7" +
    "\037\ufff7\043\ufff7\044\ufff7\047\ufff7\001\002\000\006\003" +
    "\uffe6\047\uffe6\001\002\000\004\023\027\001\002\000\006" +
    "\003\uffe8\047\uffe8\001\002\000\004\006\033\001\002\000" +
    "\004\006\032\001\002\000\030\003\ufff2\020\ufff2\021\ufff2" +
    "\025\ufff2\033\ufff2\034\ufff2\036\ufff2\037\ufff2\043\ufff2\044" +
    "\ufff2\047\ufff2\001\002\000\030\003\ufff3\020\ufff3\021\ufff3" +
    "\025\ufff3\033\ufff3\034\ufff3\036\ufff3\037\ufff3\043\ufff3\044" +
    "\ufff3\047\ufff3\001\002\000\010\003\ufff5\021\ufff5\025\ufff5" +
    "\001\002\000\004\021\211\001\002\000\010\043\025\044" +
    "\022\047\023\001\002\000\010\002\ufffb\003\ufffb\024\ufffb" +
    "\001\002\000\004\047\014\001\002\000\006\003\043\016" +
    "\044\001\002\000\004\020\057\001\002\000\004\017\056" +
    "\001\002\000\012\017\uffec\043\025\044\022\047\023\001" +
    "\002\000\004\017\055\001\002\000\004\047\014\001\002" +
    "\000\006\005\uffe9\017\uffe9\001\002\000\006\005\051\017" +
    "\uffed\001\002\000\010\043\025\044\022\047\023\001\002" +
    "\000\006\005\uffea\017\uffea\001\002\000\004\047\014\001" +
    "\002\000\006\005\uffeb\017\uffeb\001\002\000\004\020\uffef" +
    "\001\002\000\004\020\uffee\001\002\000\024\003\ufff6\020" +
    "\ufff6\033\ufff6\034\ufff6\036\ufff6\037\ufff6\043\ufff6\044\ufff6" +
    "\047\ufff6\001\002\000\024\003\066\020\071\033\ufff8\034" +
    "\063\036\061\037\062\043\025\044\022\047\067\001\002" +
    "\000\004\016\205\001\002\000\004\016\201\001\002\000" +
    "\004\016\173\001\002\000\006\007\163\022\162\001\002" +
    "\000\022\003\066\020\071\021\ufff8\033\ufff8\034\063\036" +
    "\061\037\062\047\014\001\002\000\004\006\160\001\002" +
    "\000\012\003\uffe5\007\uffc7\022\uffc7\047\uffe5\001\002\000" +
    "\004\033\074\001\002\000\020\003\066\020\071\021\ufff8" +
    "\034\063\036\061\037\062\047\014\001\002\000\004\021" +
    "\073\001\002\000\024\003\uffe4\020\uffe4\021\uffe4\033\uffe4" +
    "\034\uffe4\035\uffe4\036\uffe4\037\uffe4\047\uffe4\001\002\000" +
    "\024\010\106\013\101\014\075\016\107\041\104\042\077" +
    "\045\100\046\076\047\105\001\002\000\004\046\157\001" +
    "\002\000\030\004\uffc6\005\uffc6\006\uffc6\011\uffc6\012\uffc6" +
    "\013\uffc6\014\uffc6\015\uffc6\017\uffc6\022\uffc6\023\uffc6\001" +
    "\002\000\006\044\151\047\014\001\002\000\030\004\uffd3" +
    "\005\uffd3\006\uffd3\011\uffd3\012\uffd3\013\uffd3\014\uffd3\015" +
    "\uffd3\017\uffd3\022\uffd3\023\uffd3\001\002\000\004\046\147" +
    "\001\002\000\030\004\uffd4\005\uffd4\006\uffd4\011\uffd4\012" +
    "\uffd4\013\uffd4\014\uffd4\015\uffd4\017\uffd4\022\uffd4\023\uffd4" +
    "\001\002\000\022\004\120\006\145\011\112\012\115\013" +
    "\113\014\114\015\117\022\116\001\002\000\030\004\uffd1" +
    "\005\uffd1\006\uffd1\011\uffd1\012\uffd1\013\uffd1\014\uffd1\015" +
    "\uffd1\017\uffd1\022\uffd1\023\uffd1\001\002\000\030\004\uffd2" +
    "\005\uffd2\006\uffd2\011\uffd2\012\uffd2\013\uffd2\014\uffd2\015" +
    "\uffd2\017\uffd2\022\uffd2\023\uffd2\001\002\000\024\010\106" +
    "\013\101\014\075\016\107\041\104\042\077\045\100\046" +
    "\076\047\105\001\002\000\024\010\106\013\101\014\075" +
    "\016\107\041\104\042\077\045\100\046\076\047\105\001" +
    "\002\000\022\004\120\011\112\012\115\013\113\014\114" +
    "\015\117\017\111\022\116\001\002\000\030\004\uffcd\005" +
    "\uffcd\006\uffcd\011\uffcd\012\uffcd\013\uffcd\014\uffcd\015\uffcd" +
    "\017\uffcd\022\uffcd\023\uffcd\001\002\000\024\010\106\013" +
    "\101\014\075\016\107\041\104\042\077\045\100\046\076" +
    "\047\105\001\002\000\024\010\106\013\101\014\075\016" +
    "\107\041\104\042\077\045\100\046\076\047\105\001\002" +
    "\000\024\010\106\013\101\014\075\016\107\041\104\042" +
    "\077\045\100\046\076\047\105\001\002\000\024\010\106" +
    "\013\101\014\075\016\107\041\104\042\077\045\100\046" +
    "\076\047\105\001\002\000\024\010\106\013\101\014\075" +
    "\016\107\041\104\042\077\045\100\046\076\047\105\001" +
    "\002\000\024\010\106\013\101\014\075\016\107\041\104" +
    "\042\077\045\100\046\076\047\105\001\002\000\006\040" +
    "\122\047\014\001\002\000\006\003\123\016\124\001\002" +
    "\000\030\004\uffd7\005\uffd7\006\uffd7\011\uffd7\012\uffd7\013" +
    "\uffd7\014\uffd7\015\uffd7\017\uffd7\022\uffd7\023\uffd7\001\002" +
    "\000\004\017\134\001\002\000\026\010\106\013\101\014" +
    "\075\016\107\017\uffcb\041\104\042\077\045\100\046\076" +
    "\047\105\001\002\000\024\004\120\005\uffc8\011\112\012" +
    "\115\013\113\014\114\015\117\017\uffc8\022\116\001\002" +
    "\000\004\017\127\001\002\000\030\004\uffd6\005\uffd6\006" +
    "\uffd6\011\uffd6\012\uffd6\013\uffd6\014\uffd6\015\uffd6\017\uffd6" +
    "\022\uffd6\023\uffd6\001\002\000\006\005\131\017\uffcc\001" +
    "\002\000\024\010\106\013\101\014\075\016\107\041\104" +
    "\042\077\045\100\046\076\047\105\001\002\000\006\005" +
    "\uffc9\017\uffc9\001\002\000\024\004\120\005\uffca\011\112" +
    "\012\115\013\113\014\114\015\117\017\uffca\022\116\001" +
    "\002\000\030\004\uffd5\005\uffd5\006\uffd5\011\uffd5\012\uffd5" +
    "\013\uffd5\014\uffd5\015\uffd5\017\uffd5\022\uffd5\023\uffd5\001" +
    "\002\000\030\004\120\005\uffd9\006\uffd9\011\uffd9\012\uffd9" +
    "\013\uffd9\014\uffd9\015\uffd9\017\uffd9\022\116\023\uffd9\001" +
    "\002\000\022\004\120\011\112\012\115\013\113\014\114" +
    "\015\117\022\116\023\137\001\002\000\030\004\uffd8\005" +
    "\uffd8\006\uffd8\011\uffd8\012\uffd8\013\uffd8\014\uffd8\015\uffd8" +
    "\017\uffd8\022\uffd8\023\uffd8\001\002\000\030\004\120\005" +
    "\uffdc\006\uffdc\011\uffdc\012\uffdc\013\113\014\114\015\117" +
    "\017\uffdc\022\116\023\uffdc\001\002\000\030\004\120\005" +
    "\uffda\006\uffda\011\uffda\012\uffda\013\uffda\014\uffda\015\117" +
    "\017\uffda\022\116\023\uffda\001\002\000\030\004\120\005" +
    "\uffdb\006\uffdb\011\uffdb\012\uffdb\013\uffdb\014\uffdb\015\117" +
    "\017\uffdb\022\116\023\uffdb\001\002\000\030\004\120\005" +
    "\uffdd\006\uffdd\011\uffdd\012\115\013\113\014\114\015\117" +
    "\017\uffdd\022\116\023\uffdd\001\002\000\030\004\120\005" +
    "\uffce\006\uffce\011\uffce\012\uffce\013\uffce\014\uffce\015\uffce" +
    "\017\uffce\022\116\023\uffce\001\002\000\004\021\146\001" +
    "\002\000\010\003\ufff1\021\ufff1\025\ufff1\001\002\000\030" +
    "\004\uffc5\005\uffc5\006\uffc5\011\uffc5\012\uffc5\013\uffc5\014" +
    "\uffc5\015\uffc5\017\uffc5\022\uffc5\023\uffc5\001\002\000\004" +
    "\016\155\001\002\000\004\022\152\001\002\000\024\010" +
    "\106\013\101\014\075\016\107\041\104\042\077\045\100" +
    "\046\076\047\105\001\002\000\022\004\120\011\112\012" +
    "\115\013\113\014\114\015\117\022\116\023\154\001\002" +
    "\000\030\004\uffd0\005\uffd0\006\uffd0\011\uffd0\012\uffd0\013" +
    "\uffd0\014\uffd0\015\uffd0\017\uffd0\022\uffd0\023\uffd0\001\002" +
    "\000\004\017\156\001\002\000\030\004\uffcf\005\uffcf\006" +
    "\uffcf\011\uffcf\012\uffcf\013\uffcf\014\uffcf\015\uffcf\017\uffcf" +
    "\022\uffcf\023\uffcf\001\002\000\030\004\uffc4\005\uffc4\006" +
    "\uffc4\011\uffc4\012\uffc4\013\uffc4\014\uffc4\015\uffc4\017\uffc4" +
    "\022\uffc4\023\uffc4\001\002\000\024\003\uffde\020\uffde\021" +
    "\uffde\033\uffde\034\uffde\035\uffde\036\uffde\037\uffde\047\uffde" +
    "\001\002\000\006\021\ufff9\033\ufff9\001\002\000\024\010" +
    "\106\013\101\014\075\016\107\041\104\042\077\045\100" +
    "\046\076\047\105\001\002\000\024\010\106\013\101\014" +
    "\075\016\107\041\104\042\077\045\100\046\076\047\105" +
    "\001\002\000\022\004\120\006\165\011\112\012\115\013" +
    "\113\014\114\015\117\022\116\001\002\000\024\003\uffe0" +
    "\020\uffe0\021\uffe0\033\uffe0\034\uffe0\035\uffe0\036\uffe0\037" +
    "\uffe0\047\uffe0\001\002\000\022\004\120\011\112\012\115" +
    "\013\113\014\114\015\117\022\116\023\167\001\002\000" +
    "\004\007\170\001\002\000\024\010\106\013\101\014\075" +
    "\016\107\041\104\042\077\045\100\046\076\047\105\001" +
    "\002\000\022\004\120\006\172\011\112\012\115\013\113" +
    "\014\114\015\117\022\116\001\002\000\024\003\uffdf\020" +
    "\uffdf\021\uffdf\033\uffdf\034\uffdf\035\uffdf\036\uffdf\037\uffdf" +
    "\047\uffdf\001\002\000\024\010\106\013\101\014\075\016" +
    "\107\041\104\042\077\045\100\046\076\047\105\001\002" +
    "\000\022\004\120\011\112\012\115\013\113\014\114\015" +
    "\117\017\175\022\116\001\002\000\016\003\066\020\071" +
    "\034\063\036\061\037\062\047\014\001\002\000\004\035" +
    "\177\001\002\000\016\003\066\020\071\034\063\036\061" +
    "\037\062\047\014\001\002\000\024\003\uffe3\020\uffe3\021" +
    "\uffe3\033\uffe3\034\uffe3\035\uffe3\036\uffe3\037\uffe3\047\uffe3" +
    "\001\002\000\024\010\106\013\101\014\075\016\107\041" +
    "\104\042\077\045\100\046\076\047\105\001\002\000\022" +
    "\004\120\011\112\012\115\013\113\014\114\015\117\017" +
    "\203\022\116\001\002\000\004\006\204\001\002\000\024" +
    "\003\uffe1\020\uffe1\021\uffe1\033\uffe1\034\uffe1\035\uffe1\036" +
    "\uffe1\037\uffe1\047\uffe1\001\002\000\024\010\106\013\101" +
    "\014\075\016\107\041\104\042\077\045\100\046\076\047" +
    "\105\001\002\000\022\004\120\011\112\012\115\013\113" +
    "\014\114\015\117\017\207\022\116\001\002\000\016\003" +
    "\066\020\071\034\063\036\061\037\062\047\014\001\002" +
    "\000\024\003\uffe2\020\uffe2\021\uffe2\033\uffe2\034\uffe2\035" +
    "\uffe2\036\uffe2\037\uffe2\047\uffe2\001\002\000\010\003\ufff0" +
    "\021\ufff0\025\ufff0\001\002\000\004\020\213\001\002\000" +
    "\016\003\ufff6\021\ufff6\025\ufff6\043\ufff6\044\ufff6\047\ufff6" +
    "\001\002\000\016\003\ufff4\021\ufff4\025\ufff4\043\025\044" +
    "\022\047\023\001\002\000\010\003\035\021\216\025\036" +
    "\001\002\000\010\002\ufffc\003\ufffc\024\ufffc\001\002\000" +
    "\010\002\ufffa\003\ufffa\024\ufffa\001\002\000\004\020\221" +
    "\001\002\000\004\025\222\001\002\000\004\026\223\001" +
    "\002\000\004\027\224\001\002\000\004\030\225\001\002" +
    "\000\004\016\226\001\002\000\004\032\227\001\002\000" +
    "\004\022\230\001\002\000\004\023\231\001\002\000\004" +
    "\047\014\001\002\000\004\017\233\001\002\000\004\020" +
    "\234\001\002\000\016\003\066\020\071\034\063\036\061" +
    "\037\062\047\014\001\002\000\004\021\236\001\002\000" +
    "\004\021\237\001\002\000\010\002\uffff\003\uffff\024\uffff" +
    "\001\002\000\004\002\001\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\236\000\006\002\003\003\005\001\001\000\002\001" +
    "\001\000\004\025\217\001\001\000\004\004\006\001\001" +
    "\000\004\005\010\001\001\000\002\001\001\000\002\001" +
    "\001\000\004\025\012\001\001\000\002\001\001\000\002" +
    "\001\001\000\004\025\211\001\001\000\004\007\016\001" +
    "\001\000\010\010\017\011\023\017\020\001\001\000\004" +
    "\012\033\001\001\000\004\025\027\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\004\017\037\001\001\000" +
    "\002\001\001\000\004\025\040\001\001\000\004\013\041" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\014" +
    "\044\017\045\001\001\000\002\001\001\000\004\025\046" +
    "\001\001\000\004\016\047\001\001\000\004\015\051\001" +
    "\001\000\004\017\052\001\001\000\002\001\001\000\004" +
    "\025\053\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\004\007\057\001\001\000\014\006\067" +
    "\011\023\017\020\020\064\025\063\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\010\006\160\020\064\025\063\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\010\006\071" +
    "\020\064\025\063\001\001\000\002\001\001\000\002\001" +
    "\001\000\006\021\102\026\101\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\025\147\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\006\021\143\026" +
    "\101\001\001\000\006\021\107\026\101\001\001\000\002" +
    "\001\001\000\002\001\001\000\006\021\142\026\101\001" +
    "\001\000\006\021\141\026\101\001\001\000\006\021\140" +
    "\026\101\001\001\000\006\021\137\026\101\001\001\000" +
    "\006\021\135\026\101\001\001\000\006\021\134\026\101" +
    "\001\001\000\004\025\120\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\010\021\124\022\125" +
    "\026\101\001\001\000\004\024\127\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\023\131\001\001\000\006" +
    "\021\132\026\101\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\006\021\152\026\101\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\006\021\165\026\101\001\001\000\006\021" +
    "\163\026\101\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\006\021\170\026" +
    "\101\001\001\000\002\001\001\000\002\001\001\000\006" +
    "\021\173\026\101\001\001\000\002\001\001\000\006\020" +
    "\175\025\063\001\001\000\002\001\001\000\006\020\177" +
    "\025\063\001\001\000\002\001\001\000\006\021\201\026" +
    "\101\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\006\021\205\026\101\001\001\000\002\001" +
    "\001\000\006\020\207\025\063\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\004\007\213\001" +
    "\001\000\010\010\214\011\023\017\020\001\001\000\004" +
    "\012\033\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\004\025\231" +
    "\001\001\000\002\001\001\000\002\001\001\000\006\020" +
    "\234\025\063\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$MiniJavaParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$MiniJavaParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$MiniJavaParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
 
    return lexer.next_token(); 

    }

 
    MiniJavaLexer lexer;
    public HashMap<Object, MJToken> location;
    public MiniJavaParser(MiniJavaLexer lex) { 
        super(lex); 
        location = new HashMap<Object, MJToken>();
        lexer = lex;
    }

    public void report_error(String message, Object info) {
        if (info == null || info instanceof Symbol)
            return;

        MJToken token = (MJToken)info;
        System.out.printf(message + " at line %d, column %d\n", token.line, token.column);
    }

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$MiniJavaParser$actions {
  private final MiniJavaParser parser;

  /** Constructor */
  CUP$MiniJavaParser$actions(MiniJavaParser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$MiniJavaParser$do_action(
    int                        CUP$MiniJavaParser$act_num,
    java_cup.runtime.lr_parser CUP$MiniJavaParser$parser,
    java.util.Stack            CUP$MiniJavaParser$stack,
    int                        CUP$MiniJavaParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$MiniJavaParser$result;

      /* select the action based on the action number */
      switch (CUP$MiniJavaParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 61: // integer ::= MINUS INTEGER 
            {
              Integer RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Integer(-((Integer)(i.value)).intValue());
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("integer",20, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 60: // integer ::= PLUS INTEGER 
            {
              Integer RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = (Integer)(i.value);
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("integer",20, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 59: // integer ::= INTEGER 
            {
              Integer RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = (Integer)(i.value);
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("integer",20, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 58: // identifier ::= IDENTIFIER 
            {
              Identifier RESULT =null;
		int idleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int idright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken id = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Identifier((String)(id.value)); 
                            parser.location.put(RESULT, id);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("identifier",19, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 57: // expression_rest_list ::= 
            {
              ExpList RESULT =null;
		
                            RESULT = new ExpList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression_rest_list",18, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 56: // expression_rest_list ::= expression_rest_list expression_rest 
            {
              ExpList RESULT =null;
		int exprsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int exprsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		ExpList exprs = (ExpList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            exprs.addElement(exp);
                            RESULT = exprs;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression_rest_list",18, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 55: // expression_rest ::= COMMA expression 
            {
              Exp RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = exp;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression_rest",17, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 54: // expression_list ::= 
            {
              ExpList RESULT =null;
		
                            RESULT = new ExpList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression_list",16, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 53: // expression_list ::= expression expression_rest_list 
            {
              ExpList RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exprsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exprsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		ExpList exprs = (ExpList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new ExpList();
                            RESULT.addElement(exp);
                            for (int i = 0; i < exprs.size(); i++) {
                                RESULT.addElement(exprs.elementAt(i));
                            }
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression_list",16, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 52: // expression ::= LPAREN expression RPAREN 
            {
              Exp RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken l = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = exp;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 51: // expression ::= NOT expression 
            {
              Exp RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken n = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Not(exp);
                            parser.location.put(RESULT, n);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 50: // expression ::= NEW identifier LPAREN RPAREN 
            {
              Exp RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).right;
		MJToken n = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).value;
		int userClassleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int userClassright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Identifier userClass = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		
                            RESULT = new NewObject(userClass);
                            parser.location.put(RESULT, n);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 49: // expression ::= NEW INTTYPE LBRACKET expression RBRACKET 
            {
              Exp RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		MJToken n = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new NewArray(exp);
                            parser.location.put(RESULT, n);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 48: // expression ::= THIS 
            {
              Exp RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken t = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new This();
                            parser.location.put(RESULT, t);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 47: // expression ::= IDENTIFIER 
            {
              Exp RESULT =null;
		int varleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int varright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken var = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new IdentifierExp((String)(var.value));
                            parser.location.put(RESULT, var);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 46: // expression ::= BOOLEAN 
            {
              Exp RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken b = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new False();
                            if (((Boolean)b.value).booleanValue()) RESULT = new True();
                            parser.location.put(RESULT, b);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 45: // expression ::= integer 
            {
              Exp RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Integer i = (Integer)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new IntegerLiteral(i.intValue());
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 44: // expression ::= expression DOT identifier error RPAREN 
            {
              Exp RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int methodleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int methodright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Identifier method = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parser error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 43: // expression ::= expression DOT identifier LPAREN expression_list RPAREN 
            {
              Exp RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).value;
		int dleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int dright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		MJToken d = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int methodleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).left;
		int methodright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).right;
		Identifier method = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).value;
		int exprsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int exprsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		ExpList exprs = (ExpList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new Call(exp, method, exprs);
                            parser.location.put(RESULT, d);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 42: // expression ::= expression DOT LENGTH 
            {
              Exp RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int dleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int dright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken d = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new ArrayLength(exp);
                            parser.location.put(RESULT, d);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 41: // expression ::= expression LBRACKET expression RBRACKET 
            {
              Exp RESULT =null;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken l = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int indexleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int indexright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp index = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new ArrayLookup(exp, index);
                            parser.location.put(RESULT, l);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 40: // expression ::= expression TIMES expression 
            {
              Exp RESULT =null;
		int exp1left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int exp1right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp1 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int tleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken t = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exp2left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exp2right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp2 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Times(exp1, exp2);
                            parser.location.put(RESULT, t);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 39: // expression ::= expression MINUS expression 
            {
              Exp RESULT =null;
		int exp1left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int exp1right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp1 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int mleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int mright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken m = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exp2left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exp2right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp2 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Minus(exp1, exp2);
                            parser.location.put(RESULT, m);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 38: // expression ::= expression PLUS expression 
            {
              Exp RESULT =null;
		int exp1left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int exp1right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp1 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int pleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int pright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken p = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exp2left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exp2right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp2 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Plus(exp1, exp2);
                            parser.location.put(RESULT, p);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 37: // expression ::= expression LESSTHAN expression 
            {
              Exp RESULT =null;
		int exp1left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int exp1right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp1 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int ltleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int ltright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken lt = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exp2left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exp2right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp2 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new LessThan(exp1, exp2);
                            parser.location.put(RESULT, lt);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 36: // expression ::= expression AND expression 
            {
              Exp RESULT =null;
		int exp1left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int exp1right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp1 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int aleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int aright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MJToken a = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int exp2left = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int exp2right = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Exp exp2 = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new And(exp1, exp2);
                            parser.location.put(RESULT, a);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("expression",15, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 35: // statement ::= error SEMI 
            {
              Statement RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parse error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 34: // statement ::= identifier LBRACKET expression RBRACKET ASSIGN expression SEMI 
            {
              Statement RESULT =null;
		int varleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).left;
		int varright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).right;
		Identifier var = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).value;
		int indexleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int indexright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		Exp index = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int aleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int aright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken a = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new ArrayAssign(var, index, exp);
                            parser.location.put(RESULT, a);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 33: // statement ::= identifier ASSIGN expression SEMI 
            {
              Statement RESULT =null;
		int varleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).left;
		int varright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).right;
		Identifier var = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)).value;
		int aleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int aright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken a = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new Assign(var, exp);
                            parser.location.put(RESULT, a);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-3)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 32: // statement ::= PRINTLN LPAREN expression RPAREN SEMI 
            {
              Statement RESULT =null;
		int pleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int pright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		MJToken p = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		
                            RESULT = new Print(exp);
                            parser.location.put(RESULT, p);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 31: // statement ::= WHILE LPAREN expression RPAREN statement 
            {
              Statement RESULT =null;
		int wleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int wright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		MJToken w = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int stmtleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int stmtright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Statement stmt = (Statement)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new While(exp, stmt);
                            parser.location.put(RESULT, w);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 30: // statement ::= IF LPAREN expression RPAREN statement ELSE statement 
            {
              Statement RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int ifStmtleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int ifStmtright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Statement ifStmt = (Statement)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int elseStmtleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int elseStmtright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Statement elseStmt = (Statement)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new If(exp, ifStmt, elseStmt);
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 29: // statement ::= LCURLY statement_list RCURLY 
            {
              Statement RESULT =null;
		int lleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken l = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int statementsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int statementsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		StatementList statements = (StatementList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new Block(statements);
                            parser.location.put(RESULT, l);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement",14, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 28: // type ::= IDENTIFIER 
            {
              Type RESULT =null;
		int userClassleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int userClassright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken userClass = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new IdentifierType((String)(userClass.value));
                            parser.location.put(RESULT, userClass);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("type",13, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 27: // type ::= BOOLEANTYPE 
            {
              Type RESULT =null;
		int bleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int bright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken b = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new BooleanType();
                            parser.location.put(RESULT, b);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("type",13, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // type ::= INTTYPE 
            {
              Type RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new IntegerType();
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("type",13, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // type ::= INTTYPE LBRACKET RBRACKET 
            {
              Type RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int iright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken i = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		
                            RESULT = new IntArrayType();
                            parser.location.put(RESULT, i);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("type",13, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // formal_rest_list ::= 
            {
              FormalList RESULT =null;
		
                            RESULT = new FormalList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_rest_list",12, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // formal_rest_list ::= formal_rest_list formal_rest 
            {
              FormalList RESULT =null;
		int formalsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int formalsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		FormalList formals = (FormalList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int formalleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int formalright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Formal formal = (Formal)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            formals.addElement(formal);
                            RESULT = formals;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_rest_list",12, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // formal_rest ::= COMMA type identifier 
            {
              Formal RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		MJToken c = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int tleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Type t = (Type)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new Formal(t, name);
                            parser.location.put(RESULT, c);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_rest",11, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // formal_list ::= 
            {
              FormalList RESULT =null;
		
                            RESULT = new FormalList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_list",10, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // formal_list ::= type identifier formal_rest_list 
            {
              FormalList RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Type t = (Type)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int formalsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int formalsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		FormalList formals = (FormalList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new FormalList();
                            RESULT.addElement(new Formal(t, name));
                            for (int i = 0; i < formals.size(); i++) {
                                RESULT.addElement(formals.elementAt(i));
                            }
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_list",10, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // formal_header ::= error RPAREN 
            {
              FormalList RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parse error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_header",9, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // formal_header ::= LPAREN formal_list RPAREN 
            {
              FormalList RESULT =null;
		int formalsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int formalsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		FormalList formals = (FormalList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = formals;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("formal_header",9, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // method_decl ::= error RCURLY 
            {
              MethodDecl RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parse error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("method_decl",8, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // method_decl ::= PUBLIC type identifier formal_header LCURLY var_decl_list statement_list RETURN expression SEMI RCURLY 
            {
              MethodDecl RESULT =null;
		int pleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-10)).left;
		int pright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-10)).right;
		MJToken p = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-10)).value;
		int returnTypeleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-9)).left;
		int returnTyperight = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-9)).right;
		Type returnType = (Type)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-9)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-8)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-8)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-8)).value;
		int formalsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).left;
		int formalsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).right;
		FormalList formals = (FormalList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).value;
		int varsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).left;
		int varsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).right;
		VarDeclList vars = (VarDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).value;
		int statementsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int statementsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		StatementList statements = (StatementList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int expleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int expright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Exp exp = (Exp)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		
                            RESULT = new MethodDecl(returnType, name, formals, vars, statements, exp);
                            parser.location.put(RESULT, p);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("method_decl",8, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-10)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // var_decl ::= type error SEMI 
            {
              VarDecl RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parse error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("var_decl",7, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // var_decl ::= type identifier SEMI 
            {
              VarDecl RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int tright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Type t = (Type)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken s = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new VarDecl(t, name);
                            parser.location.put(RESULT, s); 
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("var_decl",7, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // method_decl_list ::= 
            {
              MethodDeclList RESULT =null;
		
                            RESULT = new MethodDeclList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("method_decl_list",6, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // method_decl_list ::= method_decl_list method_decl 
            {
              MethodDeclList RESULT =null;
		int methodsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int methodsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MethodDeclList methods = (MethodDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int methodDeclleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int methodDeclright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MethodDecl methodDecl = (MethodDecl)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            methods.addElement(methodDecl);
                            RESULT = methods;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("method_decl_list",6, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // var_decl_list ::= 
            {
              VarDeclList RESULT =null;
		
                            RESULT = new VarDeclList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("var_decl_list",5, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // var_decl_list ::= var_decl_list var_decl 
            {
              VarDeclList RESULT =null;
		int varsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int varsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		VarDeclList vars = (VarDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int varDeclleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int varDeclright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		VarDecl varDecl = (VarDecl)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            vars.addElement(varDecl);
                            RESULT = vars;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("var_decl_list",5, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // statement_list ::= 
            {
              StatementList RESULT =null;
		
                            RESULT = new StatementList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement_list",4, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // statement_list ::= statement statement_list 
            {
              StatementList RESULT =null;
		int stmtleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int stmtright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Statement stmt = (Statement)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int statementsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int statementsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		StatementList statements = (StatementList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            RESULT = new StatementList();
                            RESULT.addElement(stmt);
                            for (int i = 0; i < statements.size(); i++) {
                                RESULT.addElement(statements.elementAt(i));
                            }
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("statement_list",4, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // class_decl ::= error RCURLY 
            {
              ClassDecl RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		MJToken e = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		
                            parser.report_error("Parse error", e);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("class_decl",3, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // class_decl ::= CLASS identifier LCURLY var_decl_list method_decl_list RCURLY 
            {
              ClassDecl RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).right;
		MJToken c = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int varsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int varsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		VarDeclList vars = (VarDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int methodsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int methodsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MethodDeclList methods = (MethodDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new ClassDeclSimple(name, vars, methods); 
                            parser.location.put(RESULT, c);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("class_decl",3, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // class_decl ::= CLASS identifier EXTENDS identifier LCURLY var_decl_list method_decl_list RCURLY 
            {
              ClassDecl RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).right;
		MJToken c = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-6)).value;
		int parentNameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).left;
		int parentNameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).right;
		Identifier parentName = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-4)).value;
		int varsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int varsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		VarDeclList vars = (VarDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		int methodsleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int methodsright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MethodDeclList methods = (MethodDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		
                            RESULT = new ClassDeclExtends(name, parentName, vars, methods); 
                            parser.location.put(RESULT, c);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("class_decl",3, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-7)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // class_decl_list ::= 
            {
              ClassDeclList RESULT =null;
		
                            RESULT = new ClassDeclList();
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("class_decl_list",2, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // class_decl_list ::= class_decl_list class_decl 
            {
              ClassDeclList RESULT =null;
		int classesleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int classesright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		ClassDeclList classes = (ClassDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int classDeclleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int classDeclright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		ClassDecl classDecl = (ClassDecl)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		 
                            classes.addElement(classDecl);
                            RESULT = classes;
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("class_decl_list",2, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // main_class ::= CLASS identifier LCURLY PUBLIC STATIC VOID MAIN LPAREN STRING LBRACKET RBRACKET identifier RPAREN LCURLY statement RCURLY RCURLY 
            {
              MainClass RESULT =null;
		int cleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-16)).left;
		int cright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-16)).right;
		MJToken c = (MJToken)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-16)).value;
		int nameleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-15)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-15)).right;
		Identifier name = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-15)).value;
		int argleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).left;
		int argright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).right;
		Identifier arg = (Identifier)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-5)).value;
		int stmtleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).left;
		int stmtright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).right;
		Statement stmt = (Statement)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-2)).value;
		 
                            RESULT = new MainClass(name, arg, stmt); 
                            parser.location.put(RESULT, c);
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("main_class",1, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-16)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // program ::= main_class class_decl_list 
            {
              Program RESULT =null;
		int mainClassleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int mainClassright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		MainClass mainClass = (MainClass)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		int classesleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).left;
		int classesright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()).right;
		ClassDeclList classes = (ClassDeclList)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.peek()).value;
		 
                            RESULT = new Program(mainClass, classes); 
                         
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("program",0, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          return CUP$MiniJavaParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= program EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).right;
		Program start_val = (Program)((java_cup.runtime.Symbol) CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)).value;
		RESULT = start_val;
              CUP$MiniJavaParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.elementAt(CUP$MiniJavaParser$top-1)), ((java_cup.runtime.Symbol)CUP$MiniJavaParser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$MiniJavaParser$parser.done_parsing();
          return CUP$MiniJavaParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

