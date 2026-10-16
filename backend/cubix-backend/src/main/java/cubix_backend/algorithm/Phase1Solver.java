package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Thistlethwaite Phase 1 Solver.
 *
 * Goal: Orient all 12 edges (get the cube to subgroup G1).
 *
 * Strategy:
 *   - Encode edge orientation as an integer (0-2047)
 *   - Use distance table to guide a greedy search
 *   - At each step, pick the move that reduces distance the most
 *   - Result: ≤7 moves to orient all edges
 */
public class Phase1Solver {

    /**
     * Solves Phase 1 for the given cube.
     * Returns the sequence of moves that orient all edges.
     */
    public static List<Move> solve(CubieCube cube) {
        List<Move> solution = new ArrayList<>();
        CubieCube current = cube.copy();
        Move[] allMoves = EdgeOrientation.allMoves();

        // Safety limit: Phase 1 max is 7 moves; give a bit of slack
        int maxIterations = 12;

        for (int iter = 0; iter < maxIterations; iter++) {
            int currentState = EdgeOrientation.encode(current);

            // Goal reached
            if (currentState == 0) return solution;

            // Try each move; pick the one that minimizes new distance
            int bestMoveIdx = -1;
            int bestDistance = Integer.MAX_VALUE;

            for (int m = 0; m < allMoves.length; m++) {
                int nextState = EdgeOrientation.moveTable[currentState][m];
                int nextDistance = EdgeOrientation.distanceTable[nextState];

                if (nextDistance >= 0 && nextDistance < bestDistance) {
                    bestDistance = nextDistance;
                    bestMoveIdx = m;
                }
            }

            if (bestMoveIdx == -1) {
                // Should never happen — all states are reachable
                throw new IllegalStateException(
                    "Phase 1 got stuck at state " + currentState);
            }

            // Apply the best move
            current.applyMove(allMoves[bestMoveIdx]);
            solution.add(allMoves[bestMoveIdx]);
        }

        throw new IllegalStateException(
            "Phase 1 exceeded max iterations. State: " + EdgeOrientation.encode(current));
    }

    /**
     * Returns true if the cube is already in G1 (all edges oriented).
     */
    public static boolean isSolved(CubieCube cube) {
        return EdgeOrientation.encode(cube) == 0;
    }
}