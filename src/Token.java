public abstract class Token {

    private Token child;
    private Token sibling;

    public Token getChild() { return child; }
    public void setChild(Token t) { child = t; }
    public Token getSibling() { return sibling; }
    public void setSibling(Token t) { sibling = t; }

    public abstract int evalToken();
    public abstract String postfixString();
    public abstract String prefixString();
    public abstract String infixString();
    public abstract String lispString();


}
