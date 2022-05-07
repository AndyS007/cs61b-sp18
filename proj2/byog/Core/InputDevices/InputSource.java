package byog.Core.InputDevices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface InputSource {
    char getNextKey();
    //public boolean hasNextInput();
    long getSeed();
    char getOption();
    char getMovementOrInstruction();
    boolean isOver();
    boolean isMovement(char m);
    boolean endsWithSave();
    default long getPureNumber(String s) {
        String reg = "[^0-9]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(s);
        String number = m.replaceAll("").trim();
        return Long.parseLong(number);
    }
}
