package cubix_backend.algorithm;

import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import java.util.List;

public class Phase2SolverDemo {
    public static void main(String[] args) {
        System.out.println("=== Building Tables (Phase 1 + Phase 2) ===");
        EdgeOrientation.buildMoveTable();
        EdgeOrientation.buildDistanceTable();
         System.out.print("Building Phase 2 tables via BFS (this may take 60-120 seconds)... ");
Phase2Coordinates.buildTables();
System.out.println("done.");
       
        System.out.println("Tables ready.\n");

        // Test cube: scramble, solve Phase 1, then solve Phase 2
        CubieCube cube = new CubieCube();
        Move[] scramble = {
            Move.R, Move.U, Move.R_PRIME, Move.U_PRIME,
            Move.F, Move.R2, Move.B_PRIME, Move.L, Move.D2, Move.F_PRIME
        };
        System.out.print("Scramble: ");
        for (Move m : scramble) System.out.print(m.notation() + " ");
        System.out.println();

        for (Move m : scramble) cube.applyMove(m);

        // Phase 1
        List<Move> phase1 = Phase1Solver.solve(cube);
        System.out.println("\nPhase 1 (" + phase1.size() + " moves): " + phase1);
        for (Move m : phase1) cube.applyMove(m);
        System.out.println("Edges oriented? " + Phase1Solver.isSolved(cube));

        // Phase 2
        List<Move> phase2 = Phase2Solver.solve(cube);
        System.out.println("\nPhase 2 (" + phase2.size() + " moves): " + phase2);
        for (Move m : phase2) cube.applyMove(m);
        System.out.println("In G2? " + Phase2Solver.isSolved(cube));

        int total = phase1.size() + phase2.size();
        System.out.println("\n✅ Phases 1+2 solved in " + total + " moves.");
    }
}