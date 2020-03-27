import java.util.function.IntBinaryOperator;

public class OperatorToken extends Token{

    private char opChar;
    private IntBinaryOperator op;

    public OperatorToken(char c){
        opChar = c;
        if(!(c == '(' || c == ')')){
            this.op = BinaryOperator.getLambda(c);
        }
        //Parens don't have a lambda
    }

    public char getCharRepresentation() { return opChar; }
    public int getPrecedence(){
        return BinaryOperator.getPrecedence(opChar);
    }

    public boolean isOpenParen() { return opChar == '('; }
    public boolean isCloseParen() { return opChar == ')'; }

    @Override
    public int evalToken() {
        Token leftOperand = this.getChild();
        if(leftOperand == null){
            throw new IllegalStateException("Missing left operand.");
        }
        Token rightOperand = leftOperand.getSibling();
        if(rightOperand == null){
            throw new IllegalStateException("Missing right operand.");
        }
        return op.applyAsInt(leftOperand.evalToken(), rightOperand.evalToken());
    }

    @Override
    public String postfixString() {
       Token lhs = getChild();
       Token rhs = getChild().getSibling();
       return lhs.postfixString() + " " + rhs.postfixString() + " " + opChar;
    }

    @Override
    public String prefixString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return opChar + " " + lhs.prefixString() + " " + rhs.prefixString();
    }

    @Override
    public String lispString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return "( " + opChar + " " + lhs.lispString() + " " + rhs.lispString() + " )";
    }

    @Override
    public String infixString() {
        Token lhs = getChild();
        Token rhs = getChild().getSibling();
        return "( " + lhs.infixString() + " " + opChar + " " + rhs.infixString() + " )";
    }
}
