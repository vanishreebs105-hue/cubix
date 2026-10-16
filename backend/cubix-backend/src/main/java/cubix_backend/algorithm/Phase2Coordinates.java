package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Phase 2 coordinates: corner orientation + edge slice.
 *
 * The BFS visits all reachable states in the Phase 2 coordinate space.
 * (Not all nominal 1M states may be reachable.)
 */
public class Phase2Coordinates {

    public static final int TOTAL_STATES = CornerOrientation.NUM_STATES * EdgeSlice.NUM_STATES;

    public static Move[] phase2Moves() {
        return new Move[]{
            Move.U, Move.U_PRIME, Move.U2,
            Move.D, Move.D_PRIME, Move.D2,
            Move.R2, Move.L2, Move.F2, Move.B2
        };
    }

    public static int encode(CubieCube cube) {
        return CornerOrientation.encode(cube) * EdgeSlice.NUM_STATES + EdgeSlice.encode(cube);
    }

    /** Move table and distance table filled via BFS. */
    public static int[][] moveTable = new int[TOTAL_STATES][];
    public static int[] distanceTable = new int[TOTAL_STATES];
    public static CubieCube[] stateRepresentative = new CubieCube[TOTAL_STATES];

    /**
     * Build the reachable state space via BFS.
     * Fills both moveTable and distanceTable.
     */
    public static void buildTables() {
        Arrays.fill(distanceTable, -1);
        int[][] tempMoveTable = new int[TOTAL_STATES][];
        Move[] moves = phase2Moves();

        // Start: solved cube (state 0)
        CubieCube solved = new CubieCube();
        distanceTable[0] = 0;
        stateRepresentative[0] = solved;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int currentState = queue.poll();
            CubieCube currentCube = stateRepresentative[currentState];
            int[] transitions = new int[moves.length];

            for (int m = 0; m < moves.length; m++) {
                CubieCube next = currentCube.copy();
                next.applyMove(moves[m]);
                int nextState = encode(next);
                transitions[m] = nextState;

                if (distanceTable[nextState] == -1) {
                    distanceTable[nextState] = distanceTable[currentState] + 1;
                    stateRepresentative[nextState] = next;
                    queue.add(nextState);
                }
            }
            tempMoveTable[currentState] = transitions;
        }

        moveTable = tempMoveTable;

        // Report
        int reachable = 0;
        int maxDist = 0;
        for (int i = 0; i < TOTAL_STATES; i++) {
            if (distanceTable[i] >= 0) {
                reachable++;
                if (distanceTable[i] > maxDist) maxDist = distanceTable[i];
            }
        }
        System.out.println("Phase 2 BFS: " + reachable + " / " + TOTAL_STATES + " states reached.");
        System.out.println("Phase 2 max distance: " + maxDist);
    }

    // Kept for compatibility — do nothing (buildTables handles both)
    public static void buildMoveTable() { /* no-op */ }
    public static void buildDistanceTable() { /* no-op */ }
}