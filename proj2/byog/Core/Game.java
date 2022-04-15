package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        input = input.toLowerCase();
        World world = initialWorld(input);


        return world.getMap();
    }

    public long parseSeed(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; input.charAt(i) != 's'; i++) {
            sb.append(input.charAt(i));
        }
        return Long.parseLong(sb.toString());
    }

    public World initialWorld(String input) {
        switch(input.charAt(0)) {
            case 'n': {
                long seed = parseSeed(input);
                return new World(WIDTH, HEIGHT, seed);
            }
            case 'l': {
                return loadWorld();
            }
            case 'q': {
                System.exit(0);
            }
            default: {
                System.out.println("Invalid input");
                System.exit(0);
            }
        }
        return null;
    }

    public World loadWorld() {
        File file = new File("./byog/Core/world.txt");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                World loadWorld = (World) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
       return null;
    }

    public void saveWorld (World world) {
        File file = new File("./byog/Core/world.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
