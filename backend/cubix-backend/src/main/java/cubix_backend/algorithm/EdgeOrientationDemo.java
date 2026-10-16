package cubix_backend.algorithm;

public class EdgeOrientationDemo {
    public static void main(String[] args) {
        System.out.println("=== Building Edge Orientation Tables ===");
        long start = System.currentTimeMillis();

        EdgeOrientation.buildMoveTable();
        System.out.println("Move table built. Size: " + EdgeOrientation.NUM_STATES + " × 18");

        EdgeOrientation.buildDistanceTable();
        System.out.println("Distance table built.");

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + " ms\n");

        // Report max distance (the hardest state)
        int maxDist = 0;
        int maxState = 0;
        int reachable = 0;
        for (int i = 0; i < EdgeOrientation.NUM_STATES; i++) {
            if (EdgeOrientation.distanceTable[i] >= 0) {
                reachable++;
                if (EdgeOrientation.distanceTable[i] > maxDist) {
                    maxDist = EdgeOrientation.distanceTable[i];
                    maxState = i;
                }
            }
        }

        System.out.println("Reachable states: " + reachable + " / " + EdgeOrientation.NUM_STATES);
        System.out.println("Max distance: " + maxDist + " (state " + maxState + ")");
        System.out.println("\n✅ Phase 1 tables ready!");
        System.out.println("Any scrambled cube is at most " + maxDist + " moves away from all-edges-oriented.");
    }
}