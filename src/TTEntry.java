public class TTEntry {
    public static final int EXACT = 0;
    public static final int LOWER_BOUND = 1;
    public static final int UPPER_BOUND = 2;
    public final long hash;
    public final int depth;
    public final int score;
    public final int flag;
    public final Move bestMove;
    public TTEntry(long hash, int depth, int score, int flag, Move bestMove) {
        this.hash = hash;
        this.depth = depth;
        this.score = score;
        this.flag = flag;
        this.bestMove = bestMove;
    }
}