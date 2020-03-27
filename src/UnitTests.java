import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnitTests {

    @Test
    public void testSingleBinaryOps(){
        assertEquals(2, new Expression("1 + 1").evaluate());
        assertEquals(-2, new Expression("5 - 7").evaluate());
        assertEquals(56, new Expression("7 * 8").evaluate());
        assertEquals(1, new Expression("5 / 3").evaluate());
        assertEquals(256, new Expression("2 ^ 8").evaluate());
    }

    @Test
    public void testSingleBinaryOpsNoSpaces(){
        assertEquals(5, new Expression("2+3").evaluate());
        assertEquals(0, new Expression("1-1").evaluate());
        assertEquals(18, new Expression("3*6").evaluate());
        assertEquals(2, new Expression("4/2").evaluate());
        assertEquals(49, new Expression("7^2").evaluate());
    }

    @Test
    public void testStrangeSpacing(){
        assertEquals(-3, new Expression("1+2+5-2*5-3/2").evaluate());
        assertEquals(-7, new Expression( " 1 -          10+             2 * (  3 - 2)").evaluate());
        assertEquals(15, new Expression("\t\n2\n-     \t  \n\t 5 + 6\n * 3\t \t\n").evaluate());
    }

    @Test
    public void testLeftAssociativity(){
        assertEquals(6, new Expression("2 + 2 + 2").evaluate());
        assertEquals(-1, new Expression("2 + 2 - 5").evaluate());
        assertEquals(10, new Expression("5 - 1 + 6").evaluate());
        assertEquals(-8, new Expression("2 - 16 / 2 / 4 - 8"));
    }


}
