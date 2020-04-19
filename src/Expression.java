import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class represents an infix expression, storing it as an AST, and
 * allows for evaluating the expression or converting it to prefix/postfix.
 *
 * @author Samuel Laberge, 2020
 */
public class Expression {

    // A tree which represents the entire expression with correct
    // precedence of operators
    private Token expressionTree;

    /**
     * Creates an expression tree out of an infix expression string.
     *
     * @param expr An infix expression. Can include operators, operands
     *             and parentheses.
     * @throws InvalidExpressionException if the expression is malformed.
     */
    public Expression(String expr) throws InvalidExpressionException {
        CharacterStream input = new CharacterStream(expr);
        // Attempt to parse the expression into an AST
        try {
            expressionTree = getLastToken(input);
        } catch (Exception e) {
            // On failure, return an Exception with the caught exception's message.
            // Something like "Mismatch in number of operands and operators."
            throw new InvalidExpressionException(e.getLocalizedMessage());
        }
    }

    /**
     * Will evaluate the expression tree and return the integer result.
     *
     * @return the result of the evaluated expression
     */
    public int evaluate() {
        return expressionTree.evalToken();
    }

    /**
     * Runs the operator-precedence algorithm until one token remains.
     * This token is the root of the expression tree.
     *
     * @param input The input stream of the infix expression
     * @return The root of the expression tree
     */
    private Token getLastToken(CharacterStream input) {
        // The operator and operand stacks
        Deque<OperatorToken> operators = new LinkedList<>();
        Deque<Token> operands = new LinkedList<>();

        while (hasNextToken(input)) {
            Token tok = getNextToken(input);
            // If tok is an Operand, simply push it to the operands stack
            // and move on to the next token
            if (tok instanceof OperandToken) {
                operands.push(tok);
            } else if (tok instanceof OperatorToken) {
                // tok is an Operator
                OperatorToken opTok = (OperatorToken) tok;
                if (opTok.isOpenParen()) {
                    // If the token is an open parenthesis, simply push it to the
                    // operator stack
                    operators.push(opTok);
                } else if (opTok.isCloseParen()) {
                    // If the token is a close parenthesis, reduce until we find the
                    // opening parenthesis
                    while (!operators.isEmpty() && !operators.peek().isOpenParen())
                        reduce(operators, operands);

                    if (operators.isEmpty())
                        throw new IllegalArgumentException("No matching open parentheses found.");
                    // We found it, now get rid of the opening parenthesis
                    operators.pop();
                } else {
                    // For all other non-paren operators, reduce while the top of the operator
                    // stack has a higher precedence than the next operator in the input
                    while (!operators.isEmpty() && reduceForAssociativity(operators.peek(), opTok))
                        reduce(operators, operands);

                    operators.push(opTok);
                }
            } else {
                throw new IllegalStateException("Unknown token: " + tok);
            }
        }

        // Once we've run out of tokens in the input, reduce until the operator
        // stack is empty. At the end, there should be one operand remaining, the
        // root of the expression tree
        while (!operators.isEmpty())
            reduce(operators, operands);

        if (operands.size() != 1)
            throw new IllegalArgumentException("Mismatch in number of operands and operators.");

        // Return the root of the tree
        return operands.pop();
    }

    /**
     * Returns whether or not we should reduce based on the next operator in the input and
     * the operator on the top of the operators stack. For left associative operators we want
     * to reduce whenever the top of the stack has a precedence greater than or equal to the next operator.
     * For right associative, we only reduce when the precedence of the top of the stack is strictly greater
     * than the precedence of the next operator.
     *
     * @param operatorStackTop The operator on the top of the operator stack
     * @param nextOperator     The next operator in the input
     * @return true iff we should perform a reduce operation
     */
    private boolean reduceForAssociativity(OperatorToken operatorStackTop, OperatorToken nextOperator) {
        if (nextOperator.isRightAssociative()) {
            return operatorStackTop.getPrecedence() > nextOperator.getPrecedence();
        }
        // Operator must be left associative
        return operatorStackTop.getPrecedence() >= nextOperator.getPrecedence();
    }

    /**
     * The reduce subroutine. In reduce, we pop the operator off the top of the operator stack and
     * pair it with the two operands on the top of the operands stack. The resulting token is
     * pushed to the operands stack.
     *
     * @param operators The operator stack. pre: !operators.isEmpty()
     * @param operands  The operand stack. pre: operands.size() >= 2
     */
    private void reduce(Deque<OperatorToken> operators, Deque<Token> operands) {
        if (operators.isEmpty())
            throw new IllegalStateException("Missing operator to reduce");
        if (operands.isEmpty())
            throw new IllegalStateException("Missing right hand side operand");

        OperatorToken opTok = operators.pop();
        Token rhs = operands.pop();

        if (operands.isEmpty())
            throw new IllegalStateException("Missing left hand side operand");

        Token lhs = operands.pop();
        opTok.setChild(lhs);
        lhs.setSibling(rhs);
        operands.push(opTok);
    }

    /**
     * Will advance the character stream to skip all upcoming whitespace
     *
     * @param input The input stream to advance
     */
    private void skipWhitespace(CharacterStream input) {
        while (input.hasNext() && Character.isWhitespace(input.peekChar())) {
            input.nextChar();
        }
    }

    /**
     * Checks whether or not there are any more tokens left in the input stream.
     * (i.e. if there are any non-white-space characters left).
     * This method has the side effect of advancing the input stream to the next
     * non-white-space character.
     *
     * @param input The input stream to check for more tokens.
     * @return true iff there are more tokens remaining
     */
    private boolean hasNextToken(CharacterStream input) {
        skipWhitespace(input);
        return input.hasNext();
    }

    /**
     * Creates a Token object for the next token in the input stream.
     *
     * @param input The input stream to get tokens from
     * @return the token object for the next token in the input.
     * @throws NoSuchElementException if there are no more tokens in the input stream
     */
    private Token getNextToken(CharacterStream input) {
        if (!hasNextToken(input))
            throw new NoSuchElementException("No more tokens in input.");

        if (Character.isDigit(input.peekChar())) {
            return new OperandToken(parseNumber(input));
        } else if (BinaryOperator.isValidOperator(input.peekChar())) {
            return new OperatorToken(input.nextChar());
        } else {
            throw new IllegalArgumentException("Unknown token: " + input.nextChar());
        }
    }

    /**
     * Parses a positive integer String from the input stream.
     *
     * @param input the input stream. Next character should be a digit
     * @return the parsed integer
     */
    private int parseNumber(CharacterStream input) {
        if (!Character.isDigit(input.peekChar()))
            throw new IllegalStateException("Expected a number, found: " + input.peekChar());

        int num = input.nextChar() - '0';
        while (input.hasNext() && Character.isDigit(input.peekChar())) {
            num = num * 10 + (input.nextChar() - '0');
        }
        return num;
    }

    // Various methods for creating a String representation of the Expression

    /**
     * @return a prefix representation of the expression with no parentheses
     */
    public String toPrefix() {
        return expressionTree.prefixString();
    }

    /**
     * @return a prefix string with parentheses around operations
     */
    public String toLisp() {
        return expressionTree.lispString();
    }

    /**
     * @return a postfix, or RPN, representation of the expression
     */
    public String toPostfix() {
        return expressionTree.postfixString();
    }

    /**
     * @return a string similar to the provided expression String, except parentheses
     * have been added to clearly show precedence
     */
    public String toInfix() {
        return expressionTree.infixString();
    }

}
