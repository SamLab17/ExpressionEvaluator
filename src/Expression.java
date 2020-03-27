import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Expression {

    private Token expressionTree;

    public Expression(String expr) throws InvalidExpressionException {
        CharacterStream input = new CharacterStream(expr);
        try {
            expressionTree = getLastToken(input);
        } catch(Exception e){
            throw new InvalidExpressionException(e.getLocalizedMessage());
        }
    }

    public int evaluate(){
        return expressionTree.evalToken();
    }

    public String toPrefix() {
        return expressionTree.prefixString();
    }

    public String toPostfix() {
        return expressionTree.postfixString();
    }

    public String toInfix(){
        return expressionTree.infixString();
    }

    public String toLisp(){
        return expressionTree.lispString();
    }

    private Token getLastToken(CharacterStream input) {
        Deque<OperatorToken> operators = new LinkedList<>();
        Deque<Token> operands = new LinkedList<>();

        while(hasNextToken(input)){
            Token tok = getNextToken(input);
            if(tok instanceof OperandToken){
                operands.push(tok);
            } else if(tok instanceof OperatorToken){
                OperatorToken opTok = (OperatorToken) tok;
                if(opTok.isOpenParen()) {
                    operators.push(opTok);
                }
                else if(opTok.isCloseParen()){
                    while(!operators.isEmpty() && !operators.peek().isOpenParen())
                        reduce(operators, operands);
                    if(operators.isEmpty())
                        throw new IllegalArgumentException("No matching open parentheses found.");
                    operators.pop();
                } else {
                    int precedence = opTok.getPrecedence();
                    char opChar = opTok.getCharRepresentation();
                    while(!operators.isEmpty() &&
                            reduceForAssociativity(operators.peek().getPrecedence(), precedence, opChar)) {

                        reduce(operators, operands);
                    }
                    operators.push(opTok);
                }
            } else {
                throw new IllegalStateException("Unknown token: " + tok);
            }
        }

        while(!operators.isEmpty())
            reduce(operators, operands);

        if(operands.size() != 1)
            throw new IllegalArgumentException("Mismatch in number of operands and operators.");

        //System.out.println(operands.peek().toString());
        return operands.pop();
    }

    private boolean reduceForAssociativity(int stackPrecedence, int opPrecedence, char operator){
        if(operator == '^'){
            return stackPrecedence > opPrecedence;
        } else {
            return stackPrecedence >= opPrecedence;
        }
    }

    private void reduce(Deque<OperatorToken> operators, Deque<Token> operands) {
        if(operators.isEmpty())
            throw new IllegalStateException("Missing operator to reduce");
        if(operands.isEmpty())
            throw new IllegalStateException("Missing right hand side operand");

        OperatorToken opTok = operators.pop();
        Token rhs = operands.pop();

        if(operands.isEmpty())
            throw new IllegalStateException("Missing left hand side operand");

        Token lhs = operands.pop();
        opTok.setChild(lhs);
        lhs.setSibling(rhs);
        operands.push(opTok);
    }

    private void skipWhitespace(CharacterStream input){
        while(input.hasNext() && Character.isWhitespace(input.peekChar())){
            input.nextChar();
        }
    }

    private boolean hasNextToken(CharacterStream input){
        skipWhitespace(input);
        return input.hasNext();
    }

    private Token getNextToken(CharacterStream input){
        if(!hasNextToken(input))
            throw new NoSuchElementException("No more tokens in input.");

        if(Character.isDigit(input.peekChar())){
            int num = input.nextChar() - '0';
            while(input.hasNext() && Character.isDigit(input.peekChar())){
                num = num * 10 + (input.nextChar() - '0');
            }
            return new OperandToken(num);
        }

        else if(BinaryOperator.isValidOperator(input.peekChar())){
            return new OperatorToken(input.nextChar());
        }

        else {
            throw new IllegalArgumentException("Unknown token: " + input.nextChar());
        }

    }
}
