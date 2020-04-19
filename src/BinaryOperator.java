import java.util.HashMap;
import java.util.function.IntBinaryOperator;

/**
 * Describes all the BinaryOperators currently implemented in
 * this expression evaluator.
 *
 * @author Samuel Laberge, 2020
 */
public class BinaryOperator {

    private static final HashMap<Character, IntBinaryOperator> lambdas;
    private static final HashMap<Character, Integer> precedence;

    // Initialize the operator maps
    static {
        lambdas = new HashMap<>();
        precedence = new HashMap<>();

        // Defines how to evaluate each operator given two operands
        lambdas.put('+', (x, y) -> x + y);
        lambdas.put('-', (x, y) -> x - y);
        lambdas.put('*', (x, y) -> x * y);
        lambdas.put('/', (x, y) -> x / y);
        lambdas.put('^', (x, y) -> (int) Math.pow(x, y));

        // Parentheses should never cause a "reduce" to happen
        // if they are on the top of the stack and thus have the
        // smallest precedence
        precedence.put('(', 0);
        precedence.put(')', 0);

        precedence.put('+', 1);
        precedence.put('-', 1);

        precedence.put('*', 2);
        precedence.put('/', 2);

        precedence.put('^', 3);
    }

    /**
     * Gets a lambda which describes the given operator
     * i.e. getLambda('+') returns (x, y) -> x + y
     *
     * @param op the character representation of the operator
     * @return the operator lambda
     */
    public static IntBinaryOperator getLambda(char op) {
        if (!isValidOperator(op))
            throw new IllegalArgumentException(op + " is not a valid operator.");
        return lambdas.get(op);
    }

    /**
     * Gets the precedence of a given operator
     *
     * @param op the character representation of the operator
     * @return an integer representing the precedence of the operator
     */
    public static int getPrecedence(char op) {
        if (!isValidOperator(op))
            throw new IllegalArgumentException(op + " is not a valid operator.");
        return precedence.get(op);
    }

    /**
     * Checks if the given character is a valid operator
     *
     * @param c character to check
     * @return true iff c is a valid operator
     */
    public static boolean isValidOperator(char c) {
        return precedence.containsKey(c);
    }

    /**
     * Whether or not this operator is right associative. Only
     * implemented right associative operator is exponentiation.
     * i.e. 2 ^ 2 ^ 3 -> 2 ^ ( 2 ^ 3)
     *
     * @param c The character representation of this operator
     * @return true iff this operator is right associative
     * ( equiv. to true iff this operator is ^ )
     */
    public static boolean isRightAssociative(char c) {
        if (!isValidOperator(c)) {
            throw new IllegalArgumentException("Provided character is not an operator: " + c);
        }
        // Only right associative operator is exponentiation
        return c == '^';
    }

    /**
     * Whether or not this operator is left associative. All
     * operators implemented (except exponentiation) are left
     * associative. i.e. 1 + 2 + 3 -> (1 + 2) + 3
     *
     * @param c The character representation of this operator
     * @return true iff this operator is left associative
     */
    public static boolean isLeftAssociative(char c) {
        return !isRightAssociative(c);
    }

}
