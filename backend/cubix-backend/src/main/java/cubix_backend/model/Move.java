package cubix_backend.model;

/**
 * Standard Rubik's Cube moves.
 *
 * Each face has 3 rotations:
 *   - Normal    : 90° clockwise      (U)
 *   - Prime     : 90° counter-cw     (U')
 *   - Double    : 180°               (U2)
 */
public enum Move {
    // Up face
    U, U_PRIME, U2,

    // Down face
    D, D_PRIME, D2,

    // Right face
    R, R_PRIME, R2,

    // Left face
    L, L_PRIME, L2,

    // Front face
    F, F_PRIME, F2,

    // Back face
    B, B_PRIME, B2;

    /**
     * Parses a move string like "U", "R'", "F2" into a Move enum.
     * Also accepts "U3" (same as U_PRIME).
     */
    public static Move fromString(String s) {
        s = s.trim().toUpperCase();
        boolean prime = s.endsWith("'");
        boolean doubleMove = s.endsWith("2");

        char face = s.charAt(0);
        String base;
        if (prime) {
            base = face + "_PRIME";
        } else if (doubleMove) {
            base = face + "2";
        } else {
            base = String.valueOf(face);
        }

        try {
            return Move.valueOf(base);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid move: " + s);
        }
    }

    /**
     * Returns the string representation (e.g., "U", "R'", "F2").
     */
    public String notation() {
        String n = name();
        if (n.endsWith("_PRIME")) {
            return n.replace("_PRIME", "'");
        }
        return n;
    }
}