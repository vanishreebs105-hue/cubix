package cubix_backend.model;

/**
 * Cubie-level representation of a Rubik's Cube.
 *
 * - 8 corners: each has a permutation index (0-7) and orientation (0-2)
 * - 12 edges:  each has a permutation index (0-11) and orientation (0-1)
 *
 * Corner positions:          Edge positions:
 *   0: URF                     0:  UR
 *   1: UFL                     1:  UF
 *   2: ULB                     2:  UL
 *   3: UBR                     3:  UB
 *   4: DFR                     4:  DR
 *   5: DLF                     5:  DF
 *   6: DBL                     6:  DL
 *   7: DRB                     7:  DB
 *                              8:  FR
 *                              9:  FL
 *                             10:  BL
 *                             11:  BR
 *
 * Colors: U=0 White · R=1 Red · F=2 Green · D=3 Yellow · L=4 Orange · B=5 Blue
 */
public class CubieCube {

    // Corner permutation and orientation
    public byte[] cp = new byte[8];
    public byte[] co = new byte[8];

    // Edge permutation and orientation
    public byte[] ep = new byte[12];
    public byte[] eo = new byte[12];

    /** Base move definitions — standard cubie model. */
    public static final CubieCube[] MOVES = new CubieCube[6];

    static {
        // U move
        CubieCube u = new CubieCube();
        u.cp = new byte[]{3, 0, 1, 2, 4, 5, 6, 7};
        u.co = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        u.ep = new byte[]{3, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11};
        u.eo = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        MOVES[0] = u;

        // R move
        CubieCube r = new CubieCube();
        r.cp = new byte[]{4, 1, 2, 0, 7, 5, 6, 3};
        r.co = new byte[]{2, 0, 0, 1, 1, 0, 0, 2};
        r.ep = new byte[]{8, 1, 2, 3, 11, 5, 6, 7, 4, 9, 10, 0};
        r.eo = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        MOVES[1] = r;

        // F move
        CubieCube f = new CubieCube();
        f.cp = new byte[]{1, 5, 2, 3, 0, 4, 6, 7};
        f.co = new byte[]{1, 2, 0, 0, 2, 1, 0, 0};
        f.ep = new byte[]{0, 9, 2, 3, 4, 8, 6, 7, 1, 5, 10, 11};
        f.eo = new byte[]{0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0};
        MOVES[2] = f;

        // D move
        CubieCube d = new CubieCube();
        d.cp = new byte[]{0, 1, 2, 3, 5, 6, 7, 4};
        d.co = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        d.ep = new byte[]{0, 1, 2, 3, 5, 6, 7, 4, 8, 9, 10, 11};
        d.eo = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        MOVES[3] = d;

        // L move
        CubieCube l = new CubieCube();
        l.cp = new byte[]{0, 2, 6, 3, 4, 1, 5, 7};
        l.co = new byte[]{0, 1, 2, 0, 0, 2, 1, 0};
        l.ep = new byte[]{0, 1, 10, 3, 4, 5, 9, 7, 8, 2, 6, 11};
        l.eo = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        MOVES[4] = l;

        // B move
        CubieCube b = new CubieCube();
        b.cp = new byte[]{0, 1, 3, 7, 4, 5, 2, 6};
        b.co = new byte[]{0, 0, 1, 2, 0, 0, 2, 1};
        b.ep = new byte[]{0, 1, 2, 11, 4, 5, 6, 10, 8, 9, 3, 7};
        b.eo = new byte[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1};
        MOVES[5] = b;
    }

    /** Creates a solved cube. */
    public CubieCube() {
        for (byte i = 0; i < 8; i++) {
            cp[i] = i;
            co[i] = 0;
        }
        for (byte i = 0; i < 12; i++) {
            ep[i] = i;
            eo[i] = 0;
        }
    }

    /** Deep copy. */
    public CubieCube copy() {
        CubieCube c = new CubieCube();
        System.arraycopy(this.cp, 0, c.cp, 0, 8);
        System.arraycopy(this.co, 0, c.co, 0, 8);
        System.arraycopy(this.ep, 0, c.ep, 0, 12);
        System.arraycopy(this.eo, 0, c.eo, 0, 12);
        return c;
    }

    public boolean isSolved() {
        for (int i = 0; i < 8; i++) {
            if (cp[i] != i || co[i] != 0) return false;
        }
        for (int i = 0; i < 12; i++) {
            if (ep[i] != i || eo[i] != 0) return false;
        }
        return true;
    }

    /**
     * Multiplies this cube by another (composition of permutations).
     * this = this * other (applied in order: other, then this)
     */
    public void multiply(CubieCube other) {
        byte[] newCp = new byte[8];
        byte[] newCo = new byte[8];
        byte[] newEp = new byte[12];
        byte[] newEo = new byte[12];

        // Corners
        for (int i = 0; i < 8; i++) {
            newCp[i] = cp[other.cp[i]];
            newCo[i] = (byte) ((co[other.cp[i]] + other.co[i]) % 3);
        }
        // Edges
        for (int i = 0; i < 12; i++) {
            newEp[i] = ep[other.ep[i]];
            newEo[i] = (byte) ((eo[other.ep[i]] + other.eo[i]) % 2);
        }

        this.cp = newCp;
        this.co = newCo;
        this.ep = newEp;
        this.eo = newEo;
    }

    /** Applies a move to this cube (in place). */
    public void applyMove(Move move) {
        int faceIndex = faceIndexFromMove(move);
        CubieCube base = MOVES[faceIndex];

        // Determine repetitions: normal=1, prime=3, double=2
        int reps = 1;
        String name = move.name();
        if (name.endsWith("_PRIME")) reps = 3;
        else if (name.endsWith("2")) reps = 2;

        for (int i = 0; i < reps; i++) {
            this.multiply(base);
        }
    }

    private int faceIndexFromMove(Move move) {
        String n = move.name();
        char face = n.charAt(0);
        return switch (face) {
            case 'U' -> 0;
            case 'R' -> 1;
            case 'F' -> 2;
            case 'D' -> 3;
            case 'L' -> 4;
            case 'B' -> 5;
            default -> throw new IllegalArgumentException("Unknown move: " + move);
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CubieCube:\n");
        sb.append("  cp: "); for (byte b : cp) sb.append(b).append(" ");
        sb.append("\n  co: "); for (byte b : co) sb.append(b).append(" ");
        sb.append("\n  ep: "); for (byte b : ep) sb.append(b).append(" ");
        sb.append("\n  eo: "); for (byte b : eo) sb.append(b).append(" ");
        return sb.toString();
    }
}