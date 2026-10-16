package cubix_backend.api;

import java.util.List;

/**
 * Response body for /api/solve
 */
public class SolveResponse {
    private boolean success;
    private String message;
    private List<String> solution;
    private int movesUsed;
    private boolean isSolved;

    public SolveResponse() {}

    public SolveResponse(boolean success, String message, List<String> solution, int movesUsed, boolean isSolved) {
        this.success = success;
        this.message = message;
        this.solution = solution;
        this.movesUsed = movesUsed;
        this.isSolved = isSolved;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getSolution() { return solution; }
    public void setSolution(List<String> solution) { this.solution = solution; }
    public int getMovesUsed() { return movesUsed; }
    public void setMovesUsed(int movesUsed) { this.movesUsed = movesUsed; }
    public boolean isSolved() { return isSolved; }
    public void setSolved(boolean solved) { isSolved = solved; }
}