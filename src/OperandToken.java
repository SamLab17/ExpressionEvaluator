/**
 * An token which stores an operand, a literal number
 * in this case. Essentially a wrapper for an int.
 *
 * @author Samuel Laberge, 2020
 */
public class OperandToken extends Token {

    private int operand;

    public OperandToken(int value) {
        operand = value;
    }

    /**
     * The evaluation of this token is simply its value.
     *
     * @return the value of this token
     */
    @Override
    public int evalToken() {
        return operand;
    }

    // When included in String representations, operands simply
    // appear as their integer value

    @Override
    public String postfixString() {
        return operand + "";
    }

    @Override
    public String prefixString() {
        return operand + "";
    }

    @Override
    public String infixString() {
        return operand + "";
    }

    @Override
    public String lispString() {
        return operand + "";
    }

    public String toString() {
        return operand + "";
    }
}
