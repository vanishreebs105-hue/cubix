package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import java.util.List;

public class Phase1Demo {
    public static void main(String[] args) {
        System.out.println("=== Building Edge Orientation Tables ===");
        EdgeOrientation.buildMoveTable();
        EdgeOrientation.buildDistanceTable();
        System.out.println("Done.\n");

        // Test cube 1: single move scramble
        System.out.println("=== Test 1: Simple scramble ===");
        CubieCube c1 = new CubieCube();
        Move[] scramble1 = { Move.R, Move.U };
        for (Move m : scramble1) c1.applyMove(m);

        System.out.println("Scramble: R U");
        System.out.println("Phase 1 solution: " + Phase1Solver.solve(c1));

        // Apply the solution and verify
        CubieCube check1 = c1.copy();
        for (Move m : Phase1Solver.solve(c1)) check1.applyMove(m);
        System.out.println("After Phase 1, edges oriented? " + Phase1Solver.isSolved(check1));

        // Test cube 2: random scramble
        System.out.println("\n=== Test 2: Random scramble ===");
        CubieCube c2 = new CubieCube();
        Move[] scramble2 = {
            Move.R, Move.U, Move.R_PRIME, Move.U_PRIME,
            Move.F, Move.R2, Move.B_PRIME, Move.L, Move.D2, Move.F_PRIME
        };
        for (Move m : scramble2) c2.applyMove(m);

        System.out.print("Scramble: ");
        for (Move m : scramble2) System.out.print(m.notation() + " ");
        System.out.println();

        List<Move> solution2 = Phase1Solver.solve(c2);
        System.out.print("Phase 1 solution (" + solution2.size() + " moves): ");
        for (Move m : solution2) System.out.print(m.notation() + " ");
        System.out.println();

        CubieCube check2 = c2.copy();
        for (Move m : solution2) check2.applyMove(m);
        System.out.println("After Phase 1, edges oriented? " + Phase1Solver.isSolved(check2));
    }
}