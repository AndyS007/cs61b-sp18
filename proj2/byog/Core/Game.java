package byog.Core;

import byog.Core.EnumType.InputType;
import byog.Core.EnumType.Movement;
import byog.Core.InputDevices.InputSource;
import byog.Core.InputDevices.KeyboardInputSource;
import byog.Core.InputDevices.StringInputSource;
import byog.Core.WorldGeneration.Position;
import byog.Core.WorldGeneration.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    World world;
    //for simplifying the codes
    Position player;
    TETile[][] map;
    InputSource inputSource;

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    public static final int MENU_LENGTH = 50;
    public boolean playerTurn = false;

    //TODO:: check the seed 11!!!!!
    public Game() {

    }




    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.playWithKeyboard();
    }

    public World generateWorld(InputSource inputSource) {
        char option = inputSource.getOption();
        switch(option) {
            case 'n': {
                long seed = inputSource.getSeed();
                return new World(WIDTH, HEIGHT, seed);
            }
            case 'l': {
                return loadWorld();
            }
            case 'q': {
                System.exit(0);
            }
        }
        return null;
    }
    public void play(InputSource inputSource, InputType inputType) {

        if (inputType == InputType.KEYBOARD) {
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(world.getMap());
        }
        while (!inputSource.isOver()) {
            char c = inputSource.getMovementOrInstruction();
            if (inputSource.isMovement(c)) {
                movePlayer(c);
                if (inputType == InputType.KEYBOARD) {
                    ter.renderFrame(world.getMap());
                }
            }
        }
        if (inputSource.endsWithSave()) {
            saveWorld(world);
            if (inputType == InputType.KEYBOARD) {
                showMessage("Your Game have been successfully saved!");
            }
        }
        if (inputType == InputType.KEYBOARD) {
            showMessage("You End The Game");
        }


    }
    public void playWithKeyboard() {
        inputSource = new KeyboardInputSource(MENU_LENGTH);
        world = generateWorld(inputSource);
        play(inputSource, InputType.KEYBOARD);
    }

    public TETile[][] playWithInputString(String input) {
        inputSource = new StringInputSource(input);
        world = generateWorld(inputSource);
        play(inputSource, InputType.STRING);
        /*
        while (!inputSource.isGameOver()) {
            char c  = inputSource.getMovementOrInstruction();
            if (inputSource.isMovement(c)) {
                movePlayer(c);
            }
        }
        if (inputSource.endsWithSave()) {
            saveWorld(world);
        }
         */
        return world.getMap();
    }

    /*
    public void HUD() {
        int x = getMousePosition().getX();
        int y = getMousePosition().getY();
        //StdDraw.clear(Color.BLACK);
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            String description = world.getMap()[x][y].description();
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, HEIGHT, description);
            StdDraw.show();
        }

    }

     */
/*
    public Position getMousePosition() {
        Position p = new Position();
        p.x = (int) StdDraw.mouseX();
        p.y = (int) StdDraw.mouseY();
        return p;
    }

 */



    /*
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
     */


    public void handleMovements(Movement movement) {
        int nextX = world.player.nextPosition(movement).getX();
        int nextY = world.player.nextPosition(movement).getY();
        if (world.getMap()[nextX][nextY].equals(Tileset.FLOOR)) {
            world.playerMove(movement);
        } else if (world.getMap()[nextX][nextY].equals(Tileset.LOCKED_DOOR)) {
            world.getMap()[nextX][nextY] = Tileset.UNLOCKED_DOOR;
            playerTurn = false;
            showMessage("YOU WIN THE GAME");
        } else if (world.getMap()[nextX][nextY].equals(Tileset.WALL)) {
            //showMessage("You hit the wall");
        }

    }


    public void movePlayer(char movement) {
        Movement m = charToMovement(movement);
        handleMovements(m);


    }
    //input must be wasd
    private Movement charToMovement(char movement) {
        switch (movement) {
            case 'w' : {
                return Movement.UP;
            }
            case 's' : {
                return Movement.DOWN;
            }
            case 'a' : {
                return Movement.LEFT;
            }
            case 'd' : {
                return Movement.RIGHT;
            }
        }
        return null;
    }



    public void showMessage(String message) {
        int height = HEIGHT;
        int width = WIDTH;
        //StdDraw.setCanvasSize(width * 16, height * 16);
        //StdDraw.setXscale(0, width);
        //StdDraw.setXscale(0, height);
        StdDraw.clear(Color.BLACK);
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, message);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(width / 2, height / 2 - 3, "Press any keys to leave");

        StdDraw.show();
        //getOneInput();
        inputSource.getNextKey();
        System.exit(0);

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
