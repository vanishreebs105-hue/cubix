package cubix_backend.model;

public class CubieCubeDemo {
    public static void main(String[] args) {
        System.out.println("=== Cubix: CubieCube Demo ===\n");

        CubieCube cube = new CubieCube();
        System.out.println("Initial: " + cube.isSolved());

        // Apply R
        cube.applyMove(Move.R);
        System.out.println("After R: " + cube.isSolved());

        // Apply R' — should solve
        cube.applyMove(Move.R_PRIME);
        System.out.println("After R': " + cube.isSolved());

        // Apply R four times — should solve
        CubieCube c2 = new CubieCube();
        for (int i = 0; i < 4; i++) c2.applyMove(Move.R);
        System.out.println("\nR applied 4 times → solved? " + c2.isSolved());

        // Apply U four times — should solve
        CubieCube c3 = new CubieCube();
        for (int i = 0; i < 4; i++) c3.applyMove(Move.U);
        System.out.println("U applied 4 times → solved? " + c3.isSolved());

        // Apply R U R' U' six times — should solve
        CubieCube c4 = new CubieCube();
        Move[] sexy = { Move.R, Move.U, Move.R_PRIME, Move.U_PRIME };
        for (int i = 0; i < 6; i++) {
            for (Move m : sexy) c4.applyMove(m);
        }
        System.out.println("Sexy move × 6 → solved? " + c4.isSolved());

        // Scramble and see the state
        System.out.println("\n=== Scrambled cube state ===");
        CubieCube c5 = new CubieCube();
        Move[] scramble = { Move.R, Move.U, Move.R_PRIME, Move.U_PRIME, Move.F, Move.R2 };
        for (Move m : scramble) c5.applyMove(m);
        System.out.println(c5);
        System.out.println("Is solved? " + c5.isSolved());
    }
}