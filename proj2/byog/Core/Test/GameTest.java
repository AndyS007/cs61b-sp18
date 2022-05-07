package byog.Core.Test;

import byog.Core.Game;
import byog.Core.InputDevices.InputSource;
import byog.Core.InputDevices.KeyboardInputSource;
import byog.Core.InputDevices.StringInputSource;
import byog.Core.WorldGeneration.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    Game game = new Game();
    TERenderer tr = new TERenderer();
    InputSource input = new StringInputSource("");

    @Test
    public void test() {

    }


    @Test
    public void playWithKeyboard() {
        game.playWithKeyboard();
    }

    @Test
    public void playerMoveTest() {
        TETile[][] map = game.playWithInputString("n11s");
        tr.initialize(Game.WIDTH, Game.HEIGHT);
        tr.renderFrame(map);
        StdDraw.pause(1000);
        for (int i = 0; i < 8; i++) {
           // game.movePlayer("w");
            tr.renderFrame(map);
            StdDraw.pause(1000);
        }
    }
    @Test
    public void getSeedTest() {
    }
    @Test
    public void playWithLoad() {
        TETile[][] map = game.playWithInputString("l");

        tr.initialize(Game.WIDTH, Game.HEIGHT);
        tr.renderFrame(map);
        StdDraw.pause(1000);

    }
    @Test
    public void playWithSeed() {
       TETile[][] map = game.playWithInputString("n11s");
        tr.initialize(Game.WIDTH, Game.HEIGHT);
        tr.renderFrame(map);
        StdDraw.pause(1000);
    }
    @Test
    public void playWithWrongInput() {
        game.playWithInputString("q");
    }
    /*
    @Test
    public void parseSeedTest() {
        long seed = game.parseSeed("n111s");
        assertEquals(seed, 111);
        long seed2= game.parseSeed("n-1s");
        assertEquals(seed2, -1);
    }

     */

    @Test
    public void saveAndLoadWorld() {
        World world = new World(Game.WIDTH,Game.HEIGHT,10);
        tr.initialize(Game.WIDTH, Game.HEIGHT);
        tr.renderFrame(world.getMap());
        StdDraw.pause(1000);
        game.saveWorld(world);

        //tr.renderFrame(game.loadWorld().getMap());
        //StdDraw.pause(1000);

    }

    @Test
    public void drawMenuTest() {
        StdDraw.pause(2000);
    }
}