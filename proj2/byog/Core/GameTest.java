package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    Game game = new Game();
    TERenderer tr = new TERenderer();


    @Test
    public void playWithKeyboard() {
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
       TETile[][] map = game.playWithInputString("n111s");
        tr.initialize(Game.WIDTH, Game.HEIGHT);
        tr.renderFrame(map);
        StdDraw.pause(1000);
    }
    @Test
    public void playWithWrongInput() {
        game.playWithInputString("q");
    }
    @Test
    public void parseSeedTest() {
        long seed = game.parseSeed("n111s");
        assertEquals(seed, 111);
        long seed2= game.parseSeed("n-1s");
        assertEquals(seed2, -1);
    }

    @Test
    public void saveAndLoadWorld() {
        World world = new World(79,29,10);
        tr.initialize(80, 30);
        tr.renderFrame(world.getMap());
        StdDraw.pause(1000);
        game.saveWorld(world);
        System.out.println("--------------");

        //tr.renderFrame(game.loadWorld().getMap());
        //StdDraw.pause(1000);

    }
}