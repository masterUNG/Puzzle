package net.galliliu.jigsawpuzzle.presenter;

import android.util.Log;

import java.util.Random;

/**
 * Created by galliliu on 16/5/24.
 */
public class Puzzle {
    // Indicate the blank puzzle element.
    public static final int BLANK_NUMBER = 0;
    /**
     * Random generate a new puzzle with the given type.
     * @param type type
     * @return a new puzzle
     */
    public static int[] randomGeneratePuzzle(int type) {
        int length = type * type;
        int[] puzzle = new int[length];
        int num;
        Random random = new Random();

        do {
            int k = 0;
            do {
                do {
                    num = random.nextInt(length);
                } while (isNumInArray(puzzle, k, num));
                puzzle[k++] = num;
            } while (k < length);
        } while (!isAvailablePuzzle(puzzle, type));

        return puzzle;
    }

    /**
     * True if the specified num was in the given array, false otherwise.
     * @param array Array
     * @param currentPos The given range upper bound
     * @param num The number which will be checked.
     * @return True if the specified num was in the given array, false otherwise.
     */
    public static boolean isNumInArray(int[] array, int currentPos, int num) {
        for (int i = 0; i < currentPos; i++) {
            if (array[i] == num) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the specified array is a available puzzle, false otherwise.
     * @param puzzle The array which will be checked.
     * @param type type
     * @return Returns true if the specified array is a available puzzle, false otherwise.
     */
    public static boolean isAvailablePuzzle(int[] puzzle, int type) {
        // Inversion variable value
        int sumOfT = 0;
        int row = 0;

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == BLANK_NUMBER) {
                row = i;
                break;
            }
        }

        for (int i = 0; i < puzzle.length; i++) {
            Log.i("Puzzle", "puzzle: " + puzzle[i]);
            if (i == row) {
                continue;
            }

            int t = 0;
            for (int j = i + 1; j < puzzle.length; j++) {
                if (j == row) {
                    continue;
                }
                if (puzzle[j] < puzzle[i]) {
                    t++;
                }
            }

            sumOfT += t;
        }

        Log.i("Puzzle sumOfT", "Row: " + row + " sumOfT: " + sumOfT);

        if (type % 2 == 0) {
            if ((type - row / type) % 2 == 0) {
                return sumOfT % 2 != 0;
            } else {
                return sumOfT % 2 == 0;
            }
        } else {
            return sumOfT % 2 == 0;
        }
    }
}
