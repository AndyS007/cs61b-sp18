package byog.Core.InputDevices;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class KeyboardInputSource implements InputSource{
    int menuLen;
    boolean activateSaveMode;
    boolean gameOver;
    boolean save;


    public KeyboardInputSource(int menuLen) {
        this.menuLen = menuLen;
        drawMainMenu();
        gameOver = false;
        save = false;

    }
    public char getMovementOrInstruction() {
        char c = getNextKey();
        if (c == 'q'){
            gameOver = true;
            if (activateSaveMode) {
                save = true;
            }
        } else if (c == ':'){
            activateSaveMode = true;
        } else {
            activateSaveMode = false;
        }
        return c;
    }

    private boolean isOption(char option) {
        return option == 'n' || option == 'l' || option == 'q';
    }
    public char getOption() {
        char c;
        do {
            c = getNextKey();
        } while (!isOption(c));
        return c;
    }


    public boolean isOver() {
        return gameOver;
    }
    public boolean isMovement(char c) {
        return c == 'w' || c == 'a' || c == 's' || c == 'd';

    }

    @Override
    public boolean endsWithSave() {
        return save;
    }

    //all input char will be lowercase
    @Override
    public char getNextKey() {
        while (true) {
            StdDraw.pause(10);
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                return c;
            }
        }
    }


    @Override
    public long getSeed() {
        String ENTER_SEED = "Enter Seed Number";
        StringBuilder sb = new StringBuilder();
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text((double) menuLen / 2, (double) menuLen / 2, ENTER_SEED);
            StdDraw.text((double) menuLen / 2, (double) menuLen / 2 - 2, sb.toString());
            StdDraw.show();
            char input = getNextKey();
            if (input == 's') {
                break;
            }
            sb.append(input);
        }
        return getPureNumber(sb.toString());
    }
    public void drawMainMenu() {
        int height = menuLen;
        int width = menuLen;
        int titleHeight = height * 2 / 3;
        int midWidth = width / 2;
        int midHeight = height / 2;
        int titleSize = 16;
        StdDraw.setCanvasSize(height * titleSize, width * titleSize);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        String TITLE = "CS61B: THE GAME";
        String NEW_GAME = "New Game (N)";
        String LOAD_GAME = "Load Game (L)";
        String QUIT = "Quit (Q)";
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, titleHeight, TITLE);
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight, LOAD_GAME);
        StdDraw.text(midWidth, midHeight - 2, QUIT);
        StdDraw.text(midWidth, midHeight + 2, NEW_GAME);
        StdDraw.show();
    }
}
