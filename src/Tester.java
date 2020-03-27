import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Tester {

    public static void main(String[] args){
        displayTest("3 - 7 * ( 4 + ( 25 / (3 + 2)) - 2)", -46);

        System.out.println("Running unit tests.");
        Result testResults = JUnitCore.runClasses(UnitTests.class);

        for(Failure failure : testResults.getFailures())
            System.out.println(failure);

        int numTests =  testResults.getRunCount();
        int numFailed = testResults.getFailureCount();
        System.out.println("~~~~~ Test results ~~~~~");
        System.out.println("Tests passed: " + (numTests - numFailed));
        System.out.println("Tests failed: " + numFailed);
    }

    private static void displayTest(String expression, int expected){
        Expression e = new Expression(expression);
        System.out.println("Expression: " + expression);
        System.out.println("Answer: " + e.evaluate() + " (Expected " + expected +")");
        System.out.println("Infix   : " + e.toInfix());
        System.out.println("Postfix : " + e.toPostfix());
        System.out.println("Prefix  : " + e.toPrefix());
        System.out.println("Lisp    : " + e.toLisp());
    }
}
