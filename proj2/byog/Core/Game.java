package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    World world;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MENULENGTH = 40;

    //TODO:: check the seed 11!!!!!
    /*TODO: 1.实现HUD


     */

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.playWithKeyboard();
        //game.ter.renderFrame(game.playWithInputString("n1s"));;
    }
    public void playWithKeyboard() {
        drawMainMenu();
        String menuChoice = getOneInput();
        world = initialWorld(menuChoice, playChoice.playWithKeyboard);
        play();
        //showMessage("Your Game have been successfully saved!");
        System.exit(0);
    }

    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        String movements = parseWASD(input);
        world = initialWorld(input, playChoice.playWithString);
        movePlayer(movements);
        if (input.endsWith(":q")) {
            saveWorld(world);
        }

        return world.getMap();
    }


    enum playChoice{
        playWithKeyboard, playWithString
    }
    public String parseWASD(String input) {
        String movements = "wasd";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (movements.contains(Character.toString(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public void cheatCode() {
        world.getMap()[world.player.getX()][world.player.getY()] = Tileset.FLOOR;
        world.player.x = world.getDoorPosition().getX();
        world.player.y = world.getDoorPosition().getY() + 1;
        world.getMap()[world.player.getX()][world.player.getY()] = Tileset.PLAYER;
    }

    public void play() {
        ter.initialize(WIDTH, HEIGHT);
        //cheating for debugging
        //cheatCode();
        ter.renderFrame(world.getMap());
        StringBuilder quit  = new StringBuilder();
        String moveString = "wasd";
        while(!quit.toString().endsWith(":q")) {
            String movement = getOneInput();
            if (!moveString.contains(movement)) {
                quit.append(movement);
            }
            movePlayer(movement);
            ter.renderFrame(world.getMap());
        }
        saveWorld(world);

    }

    public void handleMovements(World.Movement movement) {
        int nextX = world.player.nextPosition(movement).getX();
        int nextY = world.player.nextPosition(movement).getY();
        if (world.getMap()[nextX][nextY].equals(Tileset.FLOOR)) {
            world.playerMove(movement);
        } else if (world.getMap()[nextX][nextY].equals(Tileset.LOCKED_DOOR)) {
            world.getMap()[nextX][nextY] = Tileset.UNLOCKED_DOOR;
            //showMessage("YOU WIN THE GAME");
        } else if (world.getMap()[nextX][nextY].equals(Tileset.WALL)) {
            //showMessage("You hit the wall");
        }

    }

    public void movePlayer(String movements) {

        for (int i = 0; i < movements.length(); i++) {
            char move = movements.charAt(i);
                    switch (move) {
                case 'w' :{
                    handleMovements(World.Movement.UP);
                    break;
                }
                case 'a' : {
                    handleMovements(World.Movement.LEFT);
                    break;
                }
                case 's' : {
                    handleMovements(World.Movement.DOWN);
                    break;
                }
                case 'd' : {
                    handleMovements(World.Movement.RIGHT);
                    break;
                }
                default : {
                    break;
                }
            }

        }
    }

    public String getOneInput() {
        while (true) {
            StdDraw.pause(10);
            if (StdDraw.hasNextKeyTyped()) {
                return String.valueOf(StdDraw.nextKeyTyped());
            }
        }

    }

    public void showMessage(String message) {
        int height = HEIGHT;
        int width = WIDTH;
        //StdDraw.setCanvasSize(width * 16, height * 16);
        //StdDraw.setXscale(0, width);
        //StdDraw.setXscale(0, height);
        StdDraw.clear(Color.BLACK);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, message);
        StdDraw.show();
        //StdDraw.pause(10000);

    }

    public void drawMainMenu() {
        int height = MENULENGTH;
        int width = MENULENGTH;
        int titleHeight = height * 2 / 3;
        int midWidth = width / 2;
        int midHeight = height / 2;
        ter.initialize(MENULENGTH, MENULENGTH);
        //StdDraw.clear(Color.BLACK);

        String title = "CS61B: THE GAME";
        String newGame = "New Game (N)";
        String loadGame = "Load Game (L)";
        String quit = "Quit (Q)";
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, titleHeight, title);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight, loadGame);
        StdDraw.text(midWidth, midHeight - 2, quit);
        StdDraw.text(midWidth, midHeight + 2, newGame);
        StdDraw.show();
        //StdDraw.pause(1000);
    }

    public long parseSeed(String input) {
        input = input.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; input.charAt(i) != 's'; i++) {
            sb.append(input.charAt(i));
        }
        return Long.parseLong(sb.toString());
    }


    public long getSeed() {

        String enterSeed = "Enter Seed Number";
        StringBuilder sb = new StringBuilder();
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text((double) MENULENGTH / 2, (double) MENULENGTH / 2, enterSeed);
            StdDraw.text((double) MENULENGTH / 2, (double) MENULENGTH / 2 - 2, sb.toString());
            StdDraw.show();
            String input = getOneInput();
            if (input.equals("s")) {
                break;
            }
            sb.append(input);
        }

        return Long.parseLong(sb.toString());
    }

    public World initialWorld(String input, playChoice choice) {
        switch(input.charAt(0)) {
            case 'n': {
                if (choice == playChoice.playWithString) {
                    long seed = parseSeed(input.substring(1));
                    return new World(WIDTH, HEIGHT, seed);
                } else if (choice == playChoice.playWithKeyboard) {
                    long seed = getSeed();
                    return new World(WIDTH, HEIGHT, seed);

                }
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
