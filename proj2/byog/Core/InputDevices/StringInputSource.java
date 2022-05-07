package byog.Core.InputDevices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringInputSource implements InputSource{
    private String input;
    private int index;
    private boolean movmentExist;
    public static void main(String[] args) {
        String input = "l:q";
        InputSource is = new StringInputSource(input);
        //long seed = is.getSeed();
        System.out.println(is.endsWithSave());
        //System.out.println(input);

    }

    public StringInputSource(String s) {
        input = s.toLowerCase();
        index = 0;

    }

   @Override
    public char getNextKey() {
        char c = input.charAt(index);
        index += 1;
        return c;
    }

    @Override
    public char getOption() {
        return input.charAt(0);
    }


    @Override
    public char getMovementOrInstruction() {
        return getNextKey();
    }

    @Override
    public boolean isOver() {

        return index == input.length();
    }

    @Override
    public boolean isMovement(char c) {
        return c == 'w' || c == 'a' || c == 's' || c == 'd';
    }

    @Override
    public boolean endsWithSave() {
        Pattern p = Pattern.compile("\\w+:q$");
        Matcher m = p.matcher(input);
        return m.matches();
    }


    @Override
    public long getSeed() {
        return getPureNumber(input);
    }
}
