public class KillerMoves {
    private static final int MAX_DEPTH = 64;
    private static final Move[][] killerMoves = new Move[MAX_DEPTH][2];
    public static void store(int depth, Move move) {
        if (move == null) return;
        if (move.capturedPiece != '.') return;
        if (killerMoves[depth][0] == null || !sameMove(killerMoves[depth][0], move)) {
            killerMoves[depth][1] = killerMoves[depth][0];
            killerMoves[depth][0] = move;
        }
    }
    public static boolean isKiller(int depth, Move move) {
        return sameMove(move, killerMoves[depth][0]) || sameMove(move, killerMoves[depth][1]);
    }
    private static boolean sameMove(Move a, Move b) {
        if (a == null || b == null) return false;
        return a.fromRow == b.fromRow && a.fromCol == b.fromCol && a.toRow == b.toRow && a.toCol == b.toCol;
    }
    public static void clear() {
        for (int i = 0; i < MAX_DEPTH; i++) {
            killerMoves[i][0] = null;
            killerMoves[i][1] = null;
        }
    }
}