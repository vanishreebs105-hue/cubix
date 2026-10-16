package cubix_backend.api;

import cubix_backend.algorithm.EdgeOrientation;
import cubix_backend.algorithm.Phase1Solver;
import cubix_backend.model.CubieCube;
import cubix_backend.model.Move;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

/**
 * REST API for Cubix.
 *
 * Endpoints:
 *   GET  /api/health          - Health check
 *   POST /api/solve           - Solve given scramble
 *   GET  /api/scramble        - Generate a random scramble
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SolveController {

    @PostConstruct
    public void init() {
        System.out.println("Initializing Cubix engine...");
        EdgeOrientation.buildMoveTable();
        EdgeOrientation.buildDistanceTable();
        System.out.println("Cubix engine ready.");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(
            new java.util.HashMap<String, Object>() {{
                put("success", true);
                put("message", "Cubix API is running");
                put("service", "edge-orientation-solver");
                put("timestamp", System.currentTimeMillis());
            }}
        );
    }

    @PostMapping("/solve")
    public ResponseEntity<SolveResponse> solve(@RequestBody SolveRequest request) {
        try {
            String scramble = request.getScramble();
            if (scramble == null || scramble.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new SolveResponse(false, "Scramble is required", null, 0, false)
                );
            }

            // Apply scramble to a solved cube
            CubieCube cube = new CubieCube();
            String[] tokens = scramble.trim().toUpperCase().split("\\s+");

            for (String token : tokens) {
                try {
                    Move move = Move.fromString(token);
                    cube.applyMove(move);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(
                        new SolveResponse(false, "Invalid move: " + token, null, 0, false)
                    );
                }
            }

            // Solve Phase 1 (orient all edges)
            List<Move> solution = Phase1Solver.solve(cube);

            List<String> notation = new ArrayList<>();
            for (Move m : solution) notation.add(m.notation());

            boolean solvedAfter = Phase1Solver.isSolved(cube.copy());
            // Actually verify by applying solution
            CubieCube verify = cube.copy();
            for (Move m : solution) verify.applyMove(m);
            solvedAfter = Phase1Solver.isSolved(verify);

            return ResponseEntity.ok(new SolveResponse(
                true,
                "Edge orientation solved",
                notation,
                notation.size(),
                solvedAfter
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new SolveResponse(false, "Error: " + e.getMessage(), null, 0, false)
            );
        }
    }

    @GetMapping("/scramble")
    public ResponseEntity<?> scramble(@RequestParam(defaultValue = "20") int length) {
        if (length < 1 || length > 30) length = 20;

        Move[] allMoves = EdgeOrientation.allMoves();
        java.util.Random rand = new java.util.Random();
        List<String> scramble = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Move m = allMoves[rand.nextInt(allMoves.length)];
            scramble.add(m.notation());
        }

        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("success", true);
            put("scramble", String.join(" ", scramble));
            put("length", scramble.size());
        }});
    }
}