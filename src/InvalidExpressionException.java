/**
 * An exception for a malformed expression
 *
 * @author Samuel Laberge, 2020
 */
public class InvalidExpressionException extends RuntimeException {
    public InvalidExpressionException(String reason){
        super("Invalid expression.\nReason: " + reason);
    }
}
