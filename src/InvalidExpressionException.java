public class InvalidExpressionException extends RuntimeException {
    public InvalidExpressionException(String reason){
        super("Invalid expression.\nReason: " + reason);
    }
}
