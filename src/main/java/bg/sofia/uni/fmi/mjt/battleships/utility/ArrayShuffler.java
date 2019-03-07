package bg.sofia.uni.fmi.mjt.battleships.utility;

import java.util.Random;

public class ArrayShuffler {

    // Fisher–Yates shuffle
    public static void shuffle(int[] arr) {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; --i) {
            int index = rnd.nextInt(i + 1);
            int a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }
}