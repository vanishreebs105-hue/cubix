package cubix_backend.api;

/**
 * Request body for /api/solve
 */
public class SolveRequest {
    private String scramble;

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }
}