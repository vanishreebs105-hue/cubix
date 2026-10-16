package cubix_backend.model;

/**
 * Represents a Rubik's Cube as 54 stickers arranged in 6 faces of 9.
 *
 * Face indices:
 *   0-8    U (Up)     - White
 *   9-17   R (Right)  - Red
 *   18-26  F (Front)  - Green
 *   27-35  D (Down)   - Yellow
 *   36-44  L (Left)   - Orange
 *   45-53  B (Back)   - Blue
 */
public class CubeState {

    public static final int UP = 0;
    public static final int RIGHT = 9;
    public static final int FRONT = 18;
    public static final int DOWN = 27;
    public static final int LEFT = 36;
    public static final int BACK = 45;

    private final byte[] stickers;

    /**
     * Creates a solved cube.
     */
    public CubeState() {
        this.stickers = new byte[54];
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < 9; i++) {
                stickers[face * 9 + i] = (byte) face;
            }
        }
    }

    /**
     * Creates a cube from an existing sticker array.
     */
    public CubeState(byte[] state) {
        if (state.length != 54) {
            throw new IllegalArgumentException("Cube must have exactly 54 stickers");
        }
        this.stickers = state.clone();
    }

    public CubeState copy() {
        return new CubeState(this.stickers);
    }

    public byte getSticker(int index) {
        return stickers[index];
    }

    public void setSticker(int index, byte color) {
        stickers[index] = color;
    }

    public byte[] toArray() {
        return stickers.clone();
    }

    /**
     * Returns true if each face has a uniform color.
     */
    public boolean isSolved() {
        for (int face = 0; face < 6; face++) {
            byte color = stickers[face * 9];
            for (int i = 1; i < 9; i++) {
                if (stickers[face * 9 + i] != color) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Prints the cube in a 2D layout for debugging.
     */
    public void print() {
        printRow(UP, 0);
        printRow(UP, 3);
        printRow(UP, 6);

        for (int row = 0; row < 3; row++) {
            printSideRow(LEFT, row);
            System.out.print("   ");
            printSideRow(FRONT, row);
            System.out.print("   ");
            printSideRow(RIGHT, row);
            System.out.print("   ");
            printSideRow(BACK, row);
            System.out.println();
        }

        printRow(DOWN, 0);
        printRow(DOWN, 3);
        printRow(DOWN, 6);
        System.out.println();
    }

    private void printRow(int face, int rowStart) {
        for (int i = 0; i < 9; i++) System.out.print("   ");
        System.out.print(" ");
        for (int i = 0; i < 3; i++) {
            System.out.print(colorChar(stickers[face + rowStart + i]) + "  ");
        }
        System.out.println();
    }

    private void printSideRow(int face, int row) {
        for (int i = 0; i < 3; i++) {
            System.out.print(colorChar(stickers[face + row * 3 + i]) + " ");
        }
    }

    private char colorChar(byte color) {
        return switch (color) {
            case 0 -> 'W';
            case 1 -> 'R';
            case 2 -> 'G';
            case 3 -> 'Y';
            case 4 -> 'O';
            case 5 -> 'B';
            default -> '?';
        };
    }
}