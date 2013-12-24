package com.cc.lc.interpreter;

public class TK {
    private final String name;

    // declaring constructor as private prevents outsiders
    // from creating new tokens;
    // and so can com.cc.lc.interpreter.test equality using ==.
    private TK(String name) {
        this.name = name;
    }
    public String toString() { // make it printable for debugging
        return name;
    }

    // each token is represented as a com.cc.lc.interpreter.TK object.
    public static TK LPAREN = new TK("com.cc.lc.interpreter.TK.LPAREN");       /* ( */
    public static TK RPAREN = new TK("com.cc.lc.interpreter.TK.RPAREN");       /* ) */
    public static TK QUOTE  = new TK("com.cc.lc.interpreter.TK.QUOTE");        /* ' */
    public static TK DOT    = new TK("com.cc.lc.interpreter.TK.DOT");          /* . */
    public static TK ID     = new TK("com.cc.lc.interpreter.TK.ID");           /* identifier */
    public static TK NUM    = new TK("com.cc.lc.interpreter.TK.NUM");          /* number */

    public static TK EOF    = new TK("com.cc.lc.interpreter.TK.EOF");          /* end of file */

    // com.cc.lc.interpreter.TK.ERROR special error token kind (for scanner to return to parser)
    public static final TK ERROR  = new TK("com.cc.lc.interpreter.TK.ERROR");

    public static TK none   = new TK("com.cc.lc.interpreter.TK.none");
        /* TK_none marks end of each first set in parsing. */
        /* you might not need this. */
}
