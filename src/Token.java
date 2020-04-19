/**
 * Represents a token in the Abstract Syntax Tree
 * for the expression.
 * This token can have multiple children. So the first child is stored in the child
 * instance variable and this token's sibling is stored in the sibling instance variable.
 * <p>
 * this
 * /
 * /
 * child1 ---> child2
 * <p>
 * This format allows for trees with more than 2 children (although all the operators
 * currently implemented only use 2 operands).
 *
 * @author Samuel Laberge, 2020
 */
public abstract class Token {

    private Token child;
    private Token sibling;

    public Token getChild() {
        return child;
    }

    public void setChild(Token t) {
        child = t;
    }

    public Token getSibling() {
        return sibling;
    }

    public void setSibling(Token t) {
        sibling = t;
    }

    /**
     * Evaluates this token and its subtrees.
     *
     * @return the result of the evaluation of this token
     */
    public abstract int evalToken();

    public abstract String postfixString();

    public abstract String prefixString();

    public abstract String infixString();

    public abstract String lispString();
}
