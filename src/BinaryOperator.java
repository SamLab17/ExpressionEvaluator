import java.util.HashMap;
import java.util.function.IntBinaryOperator;

public class BinaryOperator {

    private static final HashMap<Character, IntBinaryOperator> lambdas;
    private static final HashMap<Character, Integer> precedence;

    static {
        lambdas = new HashMap<>();
        precedence = new HashMap<>();

        lambdas.put('+', (x, y) -> x + y);
        lambdas.put('-', (x, y) -> x - y);
        lambdas.put('*', (x, y) -> x * y);
        lambdas.put('/', (x, y) -> x / y);
        lambdas.put('^', (x, y) -> (int) Math.pow(x, y));

        precedence.put('(', 0);
        precedence.put(')', 0);
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);
        precedence.put('^', 3);
    }

    public static IntBinaryOperator getLambda(char op){
        if(!lambdas.containsKey(op))
            throw new IllegalArgumentException(op + " is not a valid operator.");
        return lambdas.get(op);
    }

    public static int getPrecedence(char op){
        if(!precedence.containsKey(op))
            throw new IllegalArgumentException(op + " is not a valid operator.");
        return precedence.get(op);
    }

    public static boolean isValidOperator(char c){
        switch(c){
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
            case '(':
            case ')':
                return true;
            default:
                return false;
        }
    }

}
