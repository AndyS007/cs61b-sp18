package byog.lab6;

import org.junit.Test;

public class MemoryGameTest {
    MemoryGame game = new MemoryGame(40, 40, 0);
    @Test
    public void randomString() {
        String randomString = game.generateRandomString(10);
        System.out.println(randomString);
    }
    @Test
    public void testDrawFrame(){
        game.drawFrame(game.generateRandomString(10));
    }
    @Test
    public void testFlashSequence(){
        game.flashSequence(game.generateRandomString(3));
    }
    @Test
    public void testInput(){
        System.out.println(game.solicitNCharsInput(5));
    }

}
