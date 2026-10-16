package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

/**
 * Edge Orientation coordinate for Thistlethwaite Phase 1.
 *
 * An edge is "oriented" (0) or "flipped" (1).
 * We encode the orientation of all 12 edges into a single integer.
 *
 * The 12th edge's orientation is implied (must maintain even parity),
 * so we only need to track 11 edges → 2^11 = 2048 states.
 *
 * Goal: get the coordinate to 0 (all edges oriented).
 */
public class EdgeOrientation {

    public static final int NUM_STATES = 2048; // 2^11
    public static final int NUM_MOVES = 18;

    /**
     * Move table: for each state and move, what's the resulting state?
     * moveTable[state][moveIndex] = newState
     */
    public static int[][] moveTable = new int[NUM_STATES][NUM_MOVES];

    /**
     * Distance table: minimum number of moves needed to reach state 0.
     * distanceTable[state] = distance (or -1 if unreachable)
     */
    public static int[] distanceTable = new int[NUM_STATES];

    /**
     * Encodes the edge orientation of a CubieCube into a single integer (0-2047).
     */
    public static int encode(CubieCube cube) {
        int state = 0;
        for (int i = 0; i < 11; i++) {
            state = (state << 1) | cube.eo[i];
        }
        return state;
    }

    /**
     * Decodes a state integer into edge orientations.
     */
    public static byte[] decode(int state) {
        byte[] eo = new byte[12];
        for (int i = 10; i >= 0; i--) {
            eo[i] = (byte) (state & 1);
            state >>= 1;
        }
        // Last edge's orientation determined by parity
        int parity = 0;
        for (int i = 0; i < 11; i++) parity ^= eo[i];
        eo[11] = (byte) parity;
        return eo;
    }

    /**
     * Builds the move table by applying each of the 18 moves
     * to every possible edge orientation state.
     */
    public static void buildMoveTable() {
        Move[] allMoves = allMoves();

        for (int state = 0; state < NUM_STATES; state++) {
            for (int moveIdx = 0; moveIdx < NUM_MOVES; moveIdx++) {
                CubieCube cube = new CubieCube();
                cube.eo = decode(state);
                cube.applyMove(allMoves[moveIdx]);
                moveTable[state][moveIdx] = encode(cube);
            }
        }
    }

    /**
     * Builds distance table via BFS from solved state.
     * distanceTable[state] = minimum moves to reach state 0
     */
    public static void buildDistanceTable() {
        // Initialize all distances to -1 (unvisited)
        java.util.Arrays.fill(distanceTable, -1);
        distanceTable[0] = 0;

        // BFS from state 0
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDist = distanceTable[current];

            for (int moveIdx = 0; moveIdx < NUM_MOVES; moveIdx++) {
                int next = moveTable[current][moveIdx];
                if (distanceTable[next] == -1) {
                    distanceTable[next] = currentDist + 1;
                    queue.add(next);
                }
            }
        }
    }

    /**
     * Returns the 18 standard moves in consistent order.
     */
    public static Move[] allMoves() {
        return new Move[]{
            Move.U, Move.U_PRIME, Move.U2,
            Move.R, Move.R_PRIME, Move.R2,
            Move.F, Move.F_PRIME, Move.F2,
            Move.D, Move.D_PRIME, Move.D2,
            Move.L, Move.L_PRIME, Move.L2,
            Move.B, Move.B_PRIME, Move.B2
        };
    }
}