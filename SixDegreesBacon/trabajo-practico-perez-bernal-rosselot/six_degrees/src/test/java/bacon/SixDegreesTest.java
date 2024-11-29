package bacon;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;


public class SixDegreesTest {
    private final String fileName = "src/test/resources/movies.txt";
    private SixDegrees sixDegrees;

    @BeforeEach
    public void setUp(){
        sixDegrees = new SixDegrees(fileName, "/");
    }

    @Test
    public void test0() {
        assertTrue(sixDegrees.actors("Footloose (1984)").contains("Bacon, Kevin"));
        assertTrue(sixDegrees.actors("Footloose (1984)").contains("Dalton, Sam (I)"));
        assertTrue(sixDegrees.actors("Footloose (1984)").contains("Parker, Sarah Jessica"));
    }

    @Test
    public void test1() {
        assertTrue(sixDegrees.movies("Bacon, Kevin").contains("Footloose (1984)"));
        assertTrue(sixDegrees.movies("Bacon, Kevin").contains("Flatliners (1990)"));
        assertTrue(sixDegrees.movies("Bacon, Kevin").contains("Diner (1982)"));
    }

    @Test
    public void test2() {
        assertThrows(IllegalArgumentException.class, ()-> sixDegrees.actors("Disclosure"));
    }

    @Test
    public void test3() {
        assertThrows(IllegalArgumentException.class, ()-> sixDegrees.degreesOfSeparation("Kevin, Bacon", "Kevin, Bacon"));
    }

    @Test
    public void test4() {
        assertThrows(IllegalArgumentException.class, ()-> sixDegrees.coStars("Kevin, Bacon", "Kevin, Bacon"));
    }

    @Test
    public void test5() {
        assertThrows(IllegalArgumentException.class, ()-> sixDegrees.movies("Kevin, Bacon"));
    }

    @Test
    public void test6() {
        assertThrows(IllegalArgumentException.class, ()-> sixDegrees.pathOfSeparation("Kevin, Bacon", "Kevin, Bacon"));
    }

    @Test
    public void test7() {
        assertTrue(sixDegrees.degreesOfSeparation("Bacon, Kevin","Bacon, Kevin") == 0);
    }

    @Test
    public void test8() {
        assertEquals(1,sixDegrees.degreesOfSeparation("Bacon, Kevin","Silverman, Howard") );
    }

    @Test
    public void test9() {
        assertEquals(2,sixDegrees.degreesOfSeparation("Bacon, Kevin","Laskin, Michael") );
    }

    @Test
    public void test10() {
        assertEquals(3,sixDegrees.degreesOfSeparation("Bacon, Kevin","Kalfon, Jean-Pierre") );
    }

    @Test
    public void test11() {
        assertTrue(checkPathOfActors(sixDegrees.pathOfSeparation("Bacon, Kevin","Kalfon, Jean-Pierre"), "Bacon, Kevin", "Kalfon, Jean-Pierre"));
    }

    @Test
    public void test15() {
        assertNull(sixDegrees.coStars("Abbott, Tierra", "Abbott, Tommy"));
    }

    @Test
    public void test16() {
        assertNull(sixDegrees.coStars("Aames, Angela", "Aames, Marlene"));
    }

    @Test
    public void test17() {
        assertTrue(sixDegrees.coStars("Pressman, David (I)", "Trump, Donald")!= null);
    }

     @Test
    public void test18() {
        assertEquals("Zoolander (2001)", sixDegrees.coStars("Pressman, David (I)", "Trump, Donald"));
    }

    private  boolean checkPathOfActors(List<String> actorsPath, String act1, String act2) {
        if(actorsPath != null) {
            if (actorsPath.size() > 1) {
                if(act1.compareTo(actorsPath.get(0)) != 0 || act2.compareTo(actorsPath.get(actorsPath.size()-1))!=0) {
                    return false;
                }
                for (int i = 0; i < actorsPath.size() - 1; i++) {
                    if ( sixDegrees.coStars(actorsPath.get(i), actorsPath.get(i+1)) == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }









}













