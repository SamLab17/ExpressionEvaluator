import java.util.function.IntBinaryOperator;

/**
 * A token which holds an operator. This operator's
 * operands are its children. When this token is evaluated,
 * all the children are evaluated and then the operator is applied
 * to the children.
 *
 * @author Samuel Laberge, 2020
 */
public class OperatorToken extends Token {

    private char opChar;
    private IntBinaryOperator op;

    public OperatorToken(char c) {
        opChar = c;
        // Parens don't have a lambda, so this.op will == null
        // if opChar == '(' or ')'
        this.op = BinaryOperator.getLambda(c);
    }

    /**
     * Returns the precedence level of the operator. For example,
     * multiplication has higher precedence than addition, so
     * {*}.getPrecedence() > {+}.getPrecedence()
     *
     * @return an integer representing the precedence of this operator
     */
    public int getPrecedence() {
        return BinaryOperator.getPrecedence(opChar);
    }

    /**
     * Checks to see if this operator is an open parenthesis.
     * Needed to know when to stop reducing for close parentheses.
     *
     * @return true iff this operator is an open parenthesis
     */
    public boolean isOpenParen() {
        return opChar == '(';
    }

    /**
     * Checks to see if this operator is a close parenthesis
     *
     * @return true iff this operator is a close parenthesis
     */
    public boolean isCloseParen() {
        return opChar == ')';
    }

    /**
     * Whether or not this operator is left associative. All
     * operators implemented (except exponentiation) are left
     * associative. i.e. 1 + 2 + 3 -> (1 + 2) + 3
     *
     * @return true iff this operator is left associative
     */
    public boolean isLeftAssociative() {
        return BinaryOperator.isLeftAssociative(opChar);
    }

    /**
     * Whether or not this operator is right associative. Only
     * implemented right associative operator is exponentiation.
     * i.e. 2 ^ 2 ^ 3 -> 2 ^ ( 2 ^ 3)
     *
     * @return true iff this operator is right associative
     * ( equiv. to true iff this operator is ^ )
     */
    public boolean isRightAssociative() {
        return BinaryOperator.isRightAssociative(opChar);
    }

    /**
     * Evaluates the value of this operator.
     * Will first evaluate the left subtree, then the right subtree, and
     * then apply this operation to the two operands.
     *
     * @return the result of this operation on the left and right subtrees
     */
    @Override
    public int evalToken() {
        Token leftOperand = this.getChild();
        if (leftOperand == null) {
            throw new IllegalStateException("Missing left operand.");
        }
        Token rightOperand = leftOperand.getSibling();
        if (rightOperand == null) {
            throw new IllegalStateException("Missing right operand.");
        }
        return op.applyAsInt(leftOperand.evalToken(), rightOperand.evalToken());
    }

    /**
     * Creates a postfix representation of this operator
     * lhs rhs op
     *
     * @return a postfix string for this operator
     */
    @Override
    public String postfixString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return lhs.postfixString() + " " + rhs.postfixString() + " " + opChar;
    }

    /**
     * Creates a prefix representation of this operator
     * op lhs rhs
     *
     * @return a prefix string for this operator
     */
    @Override
    public String prefixString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return opChar + " " + lhs.prefixString() + " " + rhs.prefixString();
    }

    /**
     * Creates a lisp-like, prefix representation of this operator
     * (op lhs rhs)
     *
     * @return a lisp-like string for this operator
     */
    @Override
    public String lispString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return "( " + opChar + " " + lhs.lispString() + " " + rhs.lispString() + " )";
    }

    /**
     * Creates a infix representation of this operator with parentheses
     * ( lsh op rhs )
     *
     * @return an infix string for this operator
     */
    @Override
    public String infixString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return "( " + lhs.infixString() + " " + opChar + " " + rhs.infixString() + " )";
    }
}
