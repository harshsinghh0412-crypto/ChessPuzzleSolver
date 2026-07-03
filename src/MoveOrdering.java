import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoveOrdering {

    public static void orderMoves(Board board, ArrayList<Move> moves, int depth) {
        for (Move move : moves) {
            move.score = scoreMove(board, move, depth);
        }
        Collections.sort(moves, Comparator.comparingInt((Move m) -> m.score).reversed());
    }
    private static int scoreMove(Board board, Move move, int depth) {
        if (move.isEnPassant) return 9000;
        char attacker = board.board[move.fromRow][move.fromCol];
        char victim = board.board[move.toRow][move.toCol];
        if (victim == '.') {
            if (KillerMoves.isKiller(depth, move)) return 8000;
            return 0;
        }
        int attackerValue = Evaluator.getPieceValue(attacker);
        int victimValue = Evaluator.getPieceValue(victim);
        return victimValue * 10 - attackerValue;
    }

}