package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;

/**
 * Edge Slice coordinate.
 *
 * Tracks which 4 positions hold the middle-slice edges (ep values 8, 9, 10, 11).
 * C(12,4) = 495 states.
 */
public class EdgeSlice {

    public static final int NUM_STATES = 495;

    /** Encodes current slice positions into 0-494. */
    public static int encode(CubieCube cube) {
        int bits = 0;
        for (int i = 0; i < 12; i++) {
            if (cube.ep[i] >= 8) {
                bits |= (1 << i);
            }
        }
        return combinationToIndex(bits);
    }

    private static int combinationToIndex(int bits) {
        int idx = 0;
        int k = 4;
        for (int i = 11; i >= 0; i--) {
            if ((bits & (1 << i)) != 0) {
                idx += choose(i, k);
                k--;
            }
        }
        return idx;
    }

    /** Converts index 0-494 back to a 12-bit mask. */
    public static int indexToBits(int index) {
        int bits = 0;
        int k = 4;
        for (int i = 11; i >= 0 && k > 0; i--) {
            if (index >= choose(i, k)) {
                index -= choose(i, k);
                bits |= (1 << i);
                k--;
            }
        }
        return bits;
    }

    public static int choose(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return (int) result;
    }
}