package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;

public class GameTest {

    Game game = new Game();
    @Test
    public void playWithKeyboard() {
    }

    @Test
    public void playWithInputString() {
    }

    @Test
    public void saveWorld() {
        World world = new World(79,29,0);
        System.out.println(TETile.toString(world.getMap()));
        Game.saveWorld(world);
        System.out.println("--------------");
        System.out.println(TETile.toString(Game.loadWorld().getMap()));

    }
}