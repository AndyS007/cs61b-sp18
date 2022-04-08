import synthesizer.GuitarString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);


    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] string = new GuitarString[keyboard.length()];
        Set<Integer> stringPlucked = new HashSet<>();
        for (int i = 0; i < keyboard.length(); i ++) {
            string[i] = new GuitarString(440 * Math.pow(2, (i - 24.0) / 12.0));
        }


        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyboardIndex = keyboard.indexOf(key);
                if (keyboardIndex == -1) {
                    continue;
                }
                stringPlucked.add(keyboardIndex);
                string[keyboardIndex].pluck();
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int stringPlunkedNumber : stringPlucked) {
                sample += string[stringPlunkedNumber].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int stringPlunkedNumber : stringPlucked) {
                string[stringPlunkedNumber].tic();
            }
        }
    }
}

