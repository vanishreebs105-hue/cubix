package cubix_backend.model;

/**
 * Applies Rubik's Cube moves to a CubeState.
 *
 * Each face rotation affects 20 stickers:
 *   - 9 stickers on the face itself (rotate in place)
 *   - 12 stickers on the 4 adjacent faces (cycle)
 *
 * The adjacent stickers move in a 4-cycle pattern:
 *   Example for U: F-top → R-top → B-top → L-top → F-top (for clockwise)
 */
public class MoveEngine {

    /**
     * Applies a move to the cube (in place).
     */
    public static void applyMove(CubeState cube, Move move) {
        switch (move) {
            case U        -> rotateU(cube);
            case U_PRIME  -> { rotateU(cube); rotateU(cube); rotateU(cube); }
            case U2       -> { rotateU(cube); rotateU(cube); }

            case R        -> rotateR(cube);
            case R_PRIME  -> { rotateR(cube); rotateR(cube); rotateR(cube); }
            case R2       -> { rotateR(cube); rotateR(cube); }

            case F        -> rotateF(cube);
            case F_PRIME  -> { rotateF(cube); rotateF(cube); rotateF(cube); }
            case F2       -> { rotateF(cube); rotateF(cube); }

            case D        -> rotateD(cube);
            case D_PRIME  -> { rotateD(cube); rotateD(cube); rotateD(cube); }
            case D2       -> { rotateD(cube); rotateD(cube); }

            case L        -> rotateL(cube);
            case L_PRIME  -> { rotateL(cube); rotateL(cube); rotateL(cube); }
            case L2       -> { rotateL(cube); rotateL(cube); }

            case B        -> rotateB(cube);
            case B_PRIME  -> { rotateB(cube); rotateB(cube); rotateB(cube); }
            case B2       -> { rotateB(cube); rotateB(cube); }
        }
    }

    /**
     * Applies a sequence of moves separated by spaces.
     * Example: "R U R' U'" or "F2 R' B"
     */
    public static void applySequence(CubeState cube, String sequence) {
        if (sequence == null || sequence.isBlank()) return;

        String[] tokens = sequence.trim().split("\\s+");
        for (String token : tokens) {
            applyMove(cube, Move.fromString(token));
        }
    }

    // ============ FACE ROTATIONS ============

    private static void rotateU(CubeState c) {
        // Rotate U face in place (clockwise)
        rotateFaceClockwise(c, CubeState.UP);

        // Cycle top rows of adjacent faces: F → R → B → L
        // Wait — standard: F → L → B → R → F (for clockwise U)
        byte[] temp = new byte[3];

        // Save F top row
        temp[0] = c.getSticker(CubeState.FRONT + 0);
        temp[1] = c.getSticker(CubeState.FRONT + 1);
        temp[2] = c.getSticker(CubeState.FRONT + 2);

        // F ← R
        c.setSticker(CubeState.FRONT + 0, c.getSticker(CubeState.RIGHT + 0));
        c.setSticker(CubeState.FRONT + 1, c.getSticker(CubeState.RIGHT + 1));
        c.setSticker(CubeState.FRONT + 2, c.getSticker(CubeState.RIGHT + 2));

        // R ← B
        c.setSticker(CubeState.RIGHT + 0, c.getSticker(CubeState.BACK + 0));
        c.setSticker(CubeState.RIGHT + 1, c.getSticker(CubeState.BACK + 1));
        c.setSticker(CubeState.RIGHT + 2, c.getSticker(CubeState.BACK + 2));

        // B ← L
        c.setSticker(CubeState.BACK + 0, c.getSticker(CubeState.LEFT + 0));
        c.setSticker(CubeState.BACK + 1, c.getSticker(CubeState.LEFT + 1));
        c.setSticker(CubeState.BACK + 2, c.getSticker(CubeState.LEFT + 2));

        // L ← temp (saved F)
        c.setSticker(CubeState.LEFT + 0, temp[0]);
        c.setSticker(CubeState.LEFT + 1, temp[1]);
        c.setSticker(CubeState.LEFT + 2, temp[2]);
    }

    private static void rotateD(CubeState c) {
        rotateFaceClockwise(c, CubeState.DOWN);

        // Bottom rows of adjacent faces cycle: F → R → B → L → F
        byte[] temp = new byte[3];

        // Save F bottom row (indices 6,7,8)
        temp[0] = c.getSticker(CubeState.FRONT + 6);
        temp[1] = c.getSticker(CubeState.FRONT + 7);
        temp[2] = c.getSticker(CubeState.FRONT + 8);

        // F ← L
        c.setSticker(CubeState.FRONT + 6, c.getSticker(CubeState.LEFT + 6));
        c.setSticker(CubeState.FRONT + 7, c.getSticker(CubeState.LEFT + 7));
        c.setSticker(CubeState.FRONT + 8, c.getSticker(CubeState.LEFT + 8));

        // L ← B
        c.setSticker(CubeState.LEFT + 6, c.getSticker(CubeState.BACK + 6));
        c.setSticker(CubeState.LEFT + 7, c.getSticker(CubeState.BACK + 7));
        c.setSticker(CubeState.LEFT + 8, c.getSticker(CubeState.BACK + 8));

        // B ← R
        c.setSticker(CubeState.BACK + 6, c.getSticker(CubeState.RIGHT + 6));
        c.setSticker(CubeState.BACK + 7, c.getSticker(CubeState.RIGHT + 7));
        c.setSticker(CubeState.BACK + 8, c.getSticker(CubeState.RIGHT + 8));

        // R ← temp
        c.setSticker(CubeState.RIGHT + 6, temp[0]);
        c.setSticker(CubeState.RIGHT + 7, temp[1]);
        c.setSticker(CubeState.RIGHT + 8, temp[2]);
    }

    private static void rotateR(CubeState c) {
        rotateFaceClockwise(c, CubeState.RIGHT);

        // Column of U, F, D, B (right column = indices 2,5,8)
        byte[] temp = new byte[3];
        temp[0] = c.getSticker(CubeState.UP + 2);
        temp[1] = c.getSticker(CubeState.UP + 5);
        temp[2] = c.getSticker(CubeState.UP + 8);

        // U ← F
        c.setSticker(CubeState.UP + 2, c.getSticker(CubeState.FRONT + 2));
        c.setSticker(CubeState.UP + 5, c.getSticker(CubeState.FRONT + 5));
        c.setSticker(CubeState.UP + 8, c.getSticker(CubeState.FRONT + 8));

        // F ← D
        c.setSticker(CubeState.FRONT + 2, c.getSticker(CubeState.DOWN + 2));
        c.setSticker(CubeState.FRONT + 5, c.getSticker(CubeState.DOWN + 5));
        c.setSticker(CubeState.FRONT + 8, c.getSticker(CubeState.DOWN + 8));

        // D ← B (reversed — B's left column read bottom-up)
        c.setSticker(CubeState.DOWN + 2, c.getSticker(CubeState.BACK + 6));
        c.setSticker(CubeState.DOWN + 5, c.getSticker(CubeState.BACK + 3));
        c.setSticker(CubeState.DOWN + 8, c.getSticker(CubeState.BACK + 0));

        // B ← temp (reversed — write to B's left column bottom-up)
        c.setSticker(CubeState.BACK + 6, temp[0]);
        c.setSticker(CubeState.BACK + 3, temp[1]);
        c.setSticker(CubeState.BACK + 0, temp[2]);
    }

    private static void rotateL(CubeState c) {
        rotateFaceClockwise(c, CubeState.LEFT);

        // Left column (indices 0, 3, 6)
        byte[] temp = new byte[3];
        temp[0] = c.getSticker(CubeState.UP + 0);
        temp[1] = c.getSticker(CubeState.UP + 3);
        temp[2] = c.getSticker(CubeState.UP + 6);

        // U ← B (reversed)
        c.setSticker(CubeState.UP + 0, c.getSticker(CubeState.BACK + 8));
        c.setSticker(CubeState.UP + 3, c.getSticker(CubeState.BACK + 5));
        c.setSticker(CubeState.UP + 6, c.getSticker(CubeState.BACK + 2));

        // B ← D (reversed)
        c.setSticker(CubeState.BACK + 8, c.getSticker(CubeState.DOWN + 0));
        c.setSticker(CubeState.BACK + 5, c.getSticker(CubeState.DOWN + 3));
        c.setSticker(CubeState.BACK + 2, c.getSticker(CubeState.DOWN + 6));

        // D ← F
        c.setSticker(CubeState.DOWN + 0, c.getSticker(CubeState.FRONT + 0));
        c.setSticker(CubeState.DOWN + 3, c.getSticker(CubeState.FRONT + 3));
        c.setSticker(CubeState.DOWN + 6, c.getSticker(CubeState.FRONT + 6));

        // F ← temp
        c.setSticker(CubeState.FRONT + 0, temp[0]);
        c.setSticker(CubeState.FRONT + 3, temp[1]);
        c.setSticker(CubeState.FRONT + 6, temp[2]);
    }

    private static void rotateF(CubeState c) {
        rotateFaceClockwise(c, CubeState.FRONT);

        // Around F: U bottom row, R left col, D top row (reversed), L right col (reversed)
        byte[] temp = new byte[3];
        temp[0] = c.getSticker(CubeState.UP + 6);
        temp[1] = c.getSticker(CubeState.UP + 7);
        temp[2] = c.getSticker(CubeState.UP + 8);

        // U ← L (L's right column, read bottom-up)
        c.setSticker(CubeState.UP + 6, c.getSticker(CubeState.LEFT + 8));
        c.setSticker(CubeState.UP + 7, c.getSticker(CubeState.LEFT + 5));
        c.setSticker(CubeState.UP + 8, c.getSticker(CubeState.LEFT + 2));

        // L ← D (D's top row, reversed)
        c.setSticker(CubeState.LEFT + 8, c.getSticker(CubeState.DOWN + 2));
        c.setSticker(CubeState.LEFT + 5, c.getSticker(CubeState.DOWN + 1));
        c.setSticker(CubeState.LEFT + 2, c.getSticker(CubeState.DOWN + 0));

        // D ← R (R's left column, read top-down)
        c.setSticker(CubeState.DOWN + 0, c.getSticker(CubeState.RIGHT + 0));
        c.setSticker(CubeState.DOWN + 1, c.getSticker(CubeState.RIGHT + 3));
        c.setSticker(CubeState.DOWN + 2, c.getSticker(CubeState.RIGHT + 6));

        // R ← temp
        c.setSticker(CubeState.RIGHT + 0, temp[0]);
        c.setSticker(CubeState.RIGHT + 3, temp[1]);
        c.setSticker(CubeState.RIGHT + 6, temp[2]);
    }

    private static void rotateB(CubeState c) {
        rotateFaceClockwise(c, CubeState.BACK);

        byte[] temp = new byte[3];
        temp[0] = c.getSticker(CubeState.UP + 0);
        temp[1] = c.getSticker(CubeState.UP + 1);
        temp[2] = c.getSticker(CubeState.UP + 2);

        // U ← R (R's right column, top-down)
        c.setSticker(CubeState.UP + 0, c.getSticker(CubeState.RIGHT + 2));
        c.setSticker(CubeState.UP + 1, c.getSticker(CubeState.RIGHT + 5));
        c.setSticker(CubeState.UP + 2, c.getSticker(CubeState.RIGHT + 8));

        // R ← D (D's bottom row, reversed)
        c.setSticker(CubeState.RIGHT + 2, c.getSticker(CubeState.DOWN + 8));
        c.setSticker(CubeState.RIGHT + 5, c.getSticker(CubeState.DOWN + 7));
        c.setSticker(CubeState.RIGHT + 8, c.getSticker(CubeState.DOWN + 6));

        // D ← L (L's left column, top-down)
        c.setSticker(CubeState.DOWN + 6, c.getSticker(CubeState.LEFT + 0));
        c.setSticker(CubeState.DOWN + 7, c.getSticker(CubeState.LEFT + 3));
        c.setSticker(CubeState.DOWN + 8, c.getSticker(CubeState.LEFT + 6));

        // L ← temp
        c.setSticker(CubeState.LEFT + 0, temp[2]);
        c.setSticker(CubeState.LEFT + 3, temp[1]);
        c.setSticker(CubeState.LEFT + 6, temp[0]);
    }

    // ============ HELPER ============

    /**
     * Rotates the 9 stickers of a single face 90° clockwise (in place).
     */
    private static void rotateFaceClockwise(CubeState c, int faceStart) {
        byte[] s = new byte[9];
        for (int i = 0; i < 9; i++) {
            s[i] = c.getSticker(faceStart + i);
        }

        // Rotate clockwise: (0,1,2,3,4,5,6,7,8) → (6,3,0,7,4,1,8,5,2)
        c.setSticker(faceStart + 0, s[6]);
        c.setSticker(faceStart + 1, s[3]);
        c.setSticker(faceStart + 2, s[0]);
        c.setSticker(faceStart + 3, s[7]);
        c.setSticker(faceStart + 4, s[4]);
        c.setSticker(faceStart + 5, s[1]);
        c.setSticker(faceStart + 6, s[8]);
        c.setSticker(faceStart + 7, s[5]);
        c.setSticker(faceStart + 8, s[2]);
    }
}