package byog.Core;

import byog.TileEngine.TERenderer;

public class MapVisualTest {
    public static void main(String[] args) throws InterruptedException {
        TERenderer ter=new TERenderer();
        //ter.initialize(79,29);
        ter.initialize(80,30);
        MapGenerator map=new MapGenerator(80,30,284);
        ter.renderFrame(map.mapGenerator());
    }
}