public class OperandToken extends Token {

    private int operand;

    public OperandToken(int value){
        operand = value;
    }

    @Override
    public int evalToken() {
        return operand;
    }

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
    public String lispString(){
        return operand + "";
    }

    public String toString(){
        return operand + "";
    }
}
