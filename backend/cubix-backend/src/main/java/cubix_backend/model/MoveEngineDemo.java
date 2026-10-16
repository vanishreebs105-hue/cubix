package cubix_backend.model;

public class MoveEngineDemo {
    public static void main(String[] args) {
        System.out.println("=== Cubix: MoveEngine Demo ===\n");

        // Start with solved cube
        CubeState cube = new CubeState();
        System.out.println("Initial state (solved):");
        cube.print();

        // Apply one move: R (Right face 90° clockwise)
        System.out.println("After applying R:");
        MoveEngine.applyMove(cube, Move.R);
        cube.print();

        System.out.println("Is solved? " + cube.isSolved());

        // Apply "R'" (undo the R)
        System.out.println("\nAfter applying R' (should be solved again):");
        MoveEngine.applyMove(cube, Move.R_PRIME);
        cube.print();

        System.out.println("Is solved? " + cube.isSolved());

        // Test the famous "sexy move": R U R' U'
        System.out.println("\n=== Sexy Move Test: R U R' U' ===\n");
        CubeState c2 = new CubeState();
        System.out.println("Before:");
        c2.print();

        MoveEngine.applySequence(c2, "R U R' U'");
        System.out.println("After R U R' U':");
        c2.print();
        System.out.println("Is solved? " + c2.isSolved());

        // Apply sexy move 6 times — should return to solved!
        System.out.println("\n=== Applying R U R' U' SIX times (should be solved) ===\n");
        CubeState c3 = new CubeState();
        for (int i = 0; i < 6; i++) {
            MoveEngine.applySequence(c3, "R U R' U'");
        }
        c3.print();
        System.out.println("Is solved? " + c3.isSolved());
    }
}