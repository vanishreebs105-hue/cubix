package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Thistlethwaite Phase 2 Solver.
 *
 * Input:  Cube in G1 (all edges oriented)
 * Output: Cube in G2 (all corners oriented + slice edges in slice)
 *
 * Uses the BFS-built Phase 2 tables.
 */
public class Phase2Solver {

    public static List<Move> solve(CubieCube cube) {
        List<Move> solution = new ArrayList<>();
        CubieCube current = cube.copy();
        Move[] moves = Phase2Coordinates.phase2Moves();

        int maxIterations = 20;

        for (int iter = 0; iter < maxIterations; iter++) {
            int currentState = Phase2Coordinates.encode(current);

            if (currentState == 0) return solution;

            // Safety check — if this state was never reached by BFS,
            // something is wrong with the input (not in G1, or coordinate bug)
            if (Phase2Coordinates.moveTable[currentState] == null) {
                throw new IllegalStateException(
                    "Phase 2: state " + currentState + " was not reached by BFS. " +
                    "Either the input cube is not in G1, or the coordinate system has a bug.");
            }

            int bestMoveIdx = -1;
            int bestDistance = Integer.MAX_VALUE;

            int[] transitions = Phase2Coordinates.moveTable[currentState];

            for (int m = 0; m < moves.length; m++) {
                int nextState = transitions[m];
                int nextDist = Phase2Coordinates.distanceTable[nextState];

                // Skip null/unreachable states
                if (Phase2Coordinates.moveTable[nextState] == null &&
                    nextState != 0) {
                    continue;
                }

                if (nextDist >= 0 && nextDist < bestDistance) {
                    bestDistance = nextDist;
                    bestMoveIdx = m;
                }
            }

            if (bestMoveIdx == -1) {
                throw new IllegalStateException(
                    "Phase 2 stuck at state " + currentState);
            }

            current.applyMove(moves[bestMoveIdx]);
            solution.add(moves[bestMoveIdx]);
        }

        throw new IllegalStateException("Phase 2 exceeded max iterations");
    }

    public static boolean isSolved(CubieCube cube) {
        return Phase2Coordinates.encode(cube) == 0;
    }
}