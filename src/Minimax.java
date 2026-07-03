import java.util.ArrayList;

public class Minimax {
    public static final int MATE_SCORE = 100000;
    public static final int DRAW_SCORE = 0;
    public static long nodes = 0;
    public static int minimax(Board board, int depth, int alpha, int beta, boolean whiteToMove) {
        nodes++;
        long hash = board.zobristHash;
        TTEntry entry = TranspositionTable.get(hash);
        if (entry != null && entry.depth >= depth) {
            return entry.score;
        }
        if (board.isThreefoldRepetition()) {
            return DRAW_SCORE;
        }
        if (board.isInsufficientMaterial()) {
            return DRAW_SCORE;
        }
        if (depth == 0) return quiescence(board, alpha, beta, whiteToMove);
        ArrayList<Move> moves = MoveGenerator.generateLegalMoves(board, whiteToMove);
        MoveOrdering.orderMoves(board, moves, depth);
        if (moves.isEmpty()) {
            if (AttackDetector.isKingInCheck(board, whiteToMove)) {
                if (whiteToMove) return -MATE_SCORE;
                return MATE_SCORE;
            }
            return DRAW_SCORE;
        }
        if (whiteToMove) {
            int bestScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                board.makeMove(move);
                int score = minimax(board, depth - 1, alpha, beta, false);
                board.undoMove(move);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) break;
            }
            TranspositionTable.put(new TTEntry(hash, depth, bestScore, TTEntry.EXACT, null));
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                board.makeMove(move);
                int score = minimax(board, depth - 1, alpha, beta, true);
                board.undoMove(move);
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    KillerMoves.store(depth, move);
                    break;
                }
            }
            TranspositionTable.put(new TTEntry(hash, depth, bestScore, TTEntry.EXACT, null));
            return bestScore;
        }
    }

    public static Move findBestMove(Board board, int depth, boolean whiteToMove) {
        nodes = 0;
        ArrayList<Move> moves = MoveGenerator.generateLegalMoves(board, whiteToMove);
        MoveOrdering.orderMoves(board, moves, depth);
        Move bestMove = null;
        int bestScore = whiteToMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move move : moves) {
            board.makeMove(move);
            int score = minimax(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !whiteToMove);
            board.undoMove(move);
            if (whiteToMove) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    private static int quiescence(Board board, int alpha, int beta, boolean whiteToMove) {
        nodes++;
        int standPat = Evaluator.evaluate(board);
        if (whiteToMove) {
            if (standPat >= beta) return beta;
            alpha = Math.max(alpha, standPat);
        }
        else {
            if (standPat <= alpha) return alpha;
            beta = Math.min(beta, standPat);
        }
        ArrayList<Move> captures = MoveGenerator.generateCaptureMoves(board, whiteToMove);
        for (Move move : captures) {
            board.makeMove(move);
            int score = quiescence(board, alpha, beta, !whiteToMove);
            board.undoMove(move);
            if (whiteToMove) {
                alpha = Math.max(alpha, score);
                if(alpha >= beta) return beta;
            }
            else {
                beta = Math.min(beta, score);
                if(beta <= alpha) return alpha;
            }
        }
        return whiteToMove ? alpha : beta;

    }
}