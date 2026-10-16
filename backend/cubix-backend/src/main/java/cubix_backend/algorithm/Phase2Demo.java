package cubix_backend.algorithm;

public class Phase2Demo {
    public static void main(String[] args) {
        System.out.println("=== Building Phase 2 Tables ===");
        System.out.println("Total states: " + Phase2Coordinates.TOTAL_STATES);

        long start = System.currentTimeMillis();

        System.out.print("Building move table... ");
        Phase2Coordinates.buildMoveTable();
        System.out.println("done.");

        long mid = System.currentTimeMillis();
        System.out.println("Move table time: " + (mid - start) + " ms");

        System.out.print("Building distance table (BFS)... ");
        Phase2Coordinates.buildDistanceTable();
        System.out.println("done.");

        long end = System.currentTimeMillis();
        System.out.println("Distance table time: " + (end - mid) + " ms");
        System.out.println("Total: " + (end - start) + " ms\n");

        // Analyze
        int maxDist = 0;
        int reachable = 0;
        for (int i = 0; i < Phase2Coordinates.TOTAL_STATES; i++) {
            if (Phase2Coordinates.distanceTable[i] >= 0) {
                reachable++;
                maxDist = Math.max(maxDist, Phase2Coordinates.distanceTable[i]);
            }
        }

        System.out.println("Reachable states: " + reachable + " / " + Phase2Coordinates.TOTAL_STATES);
        System.out.println("Max distance: " + maxDist);
        System.out.println("\n✅ Phase 2 tables ready!");
        System.out.println("Any cube in G1 is at most " + maxDist + " moves from G2.");
    }
}