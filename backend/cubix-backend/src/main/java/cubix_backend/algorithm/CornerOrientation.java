package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;

/**
 * Corner Orientation coordinate.
 *
 * Each corner has orientation 0, 1, or 2.
 * 7 corners determine the 8th (sum of all orientations ≡ 0 mod 3).
 * So we track 3^7 = 2187 states.
 */
public class CornerOrientation {

    public static final int NUM_STATES = 2187; // 3^7

    public static int encode(CubieCube cube) {
        int state = 0;
        for (int i = 0; i < 7; i++) {
            state = state * 3 + cube.co[i];
        }
        return state;
    }

    public static byte[] decode(int state) {
        byte[] co = new byte[8];
        int sum = 0;
        for (int i = 6; i >= 0; i--) {
            co[i] = (byte) (state % 3);
            state /= 3;
            sum += co[i];
        }
        // 8th corner's orientation determined by (3 - sum % 3) % 3
        co[7] = (byte) ((3 - sum % 3) % 3);
        return co;
    }
}