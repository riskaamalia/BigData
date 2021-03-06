import kata.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 1224A on 8/16/2016.
 */
public class codeTest {

    private Player conversion = new Player();

/*
    @Test
    public void shouldCovertToRoman() {
        assertEquals("nbYear(1) should equal to I", "CMLXXXIV", conversion.nbYear(984));
        assertEquals("nbYear(4) should equal to XCI", "XCIII", conversion.nbYear(93));
        assertEquals("nbYear(6) should equal to VI", "IX", conversion.nbYear(9));
//        1000=M, 900=CM, 90=XC; 2000=MM, 8=VIII. 1666 MDCLXVI.
    }
*/

    private static void testing(int actual, int expected) {
        assertEquals(expected, actual);
    }

    @Test
    public void test1() {
        System.out.println("Fixed Tests: nbYear");
        testing(Player.nbYear(1500, 5, 100, 5000),15);
        testing(Player.nbYear(1500000, 2.5, 10000, 2000000), 10);
        testing(Player.nbYear(1500000, 0.25, 1000, 2000000), 94);
    }

    /*@Test
   public void Tests() {
        assertEquals("makeReadable(0)", "00:00:00", code.makeReadable(0));
        assertEquals("makeReadable(5)", "00:00:05", code.makeReadable(5));
        assertEquals("makeReadable(60)", "00:01:00", code.makeReadable(60));
        assertEquals("makeReadable(86399)", "23:59:59", code.makeReadable(86399));
        assertEquals("makeReadable(359999)", "99:59:59", code.makeReadable(359999));
    }*/

   /* @Test
    public void Test() {
        assertEquals("Should return true", true, code.isValid(new char[] {'n','s','n','s','n','s','n','s','n','s'}));
        assertEquals("Should return false", false, code.isValid(new char[] {'w','e','w','e','w','e','w','e','w','e','w','e'}));
        assertEquals("Should return false", false, code.isValid(new char[] {'w'}));
        assertEquals("Should return false", false, code.isValid(new char[] {'n','n','n','s','n','s','n','s','n','s'}));
    }*/
    /*@Test
    public void tests() {
        assertEquals("(123) 456-7890", kata.code.createPhoneNumber(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0}));
    }*/

    /*@Test
    public void testGetWinner_01() {
        assertThat(kata.code.getWinner(Arrays.asList("A")), is("A"));
    }

    @Test
    public void testGetWinner_02() {
        assertThat(kata.code.getWinner(Arrays.asList("A", "A", "A", "B", "B", "B", "A")), is("A"));
    }

    @Test
    public void testGetWinner_03() {
        assertThat(kata.code.getWinner(Arrays.asList("A", "A", "A", "B", "B", "B")), is(nullValue()));
    }

    @Test
    public void testGetWinner_04() {
        assertThat(kata.code.getWinner(Arrays.asList("A", "A", "A", "B", "C", "B")), is(nullValue()));
    }

    @Test
    public void testGetWinner_05() {
        assertThat(kata.code.getWinner(Arrays.asList("A", "A", "B", "B", "C")), is(nullValue()));
    }*/

    /*JadenCase jadenCase = new JadenCase();

    @Test
    public void test() {
        assertEquals("toJadenCase doesn't return a valide JadenCase String! try again please :)", jadenCase.toJadenCase("most trees are blue"), "Most Trees Are Blue");
    }

    @Test
    public void testNullArg() {
        assertNull("Must return null when the arg is null", jadenCase.toJadenCase(null));
    }

    @Test
    public void testEmptyArg() {
        assertNull("Must return null when the arg is null", jadenCase.toJadenCase(""));
    }*/
}