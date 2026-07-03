public class Evaluator {
    private static final int[][] PAWN_TABLE = {
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int[][] KNIGHT_TABLE = {

            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}

    };

    private static final int[][] BISHOP_TABLE = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private static final int[][] ROOK_TABLE = {
            { 0,  0,  5, 10, 10,  5,  0,  0},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int[][] QUEEN_TABLE = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private static final int[][] KING_MIDDLE_GAME_TABLE = new int[8][8];

    private static final int[][] KING_END_GAME_TABLE = new int[8][8];

    private static int getTableValue(int[][] table, char piece, int row, int col) {
        if (Character.isUpperCase(piece)) return table[row][col];
        return table[7 - row][col];
    }

    public static int evaluate(Board board) {
        return material(board) + pieceSquareEvaluation(board) + mobility(board) + bishopPair(board) + pawnStructure(board) + kingSafety(board);
    }

    private static int kingSafety(Board board) {
        return pawnShield(board);
    }

    private static int pawnStructure(Board board) {
        int score = 0;
        score += doubledPawns(board);
        score += isolatedPawns(board);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board.board[row][col];
                if (piece != 'P' && piece != 'p') continue;
                if (isPassedPawn(board, row, col, piece)) {
                    if (piece == 'P') {
                        score += (7 - row) * 15;
                    } else {
                        score -= row * 15;
                    }
                }
            }
        }
        return score;
    }

    private static int isolatedPawns(Board board) {
        int score = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board.board[row][col];
                if (piece != 'P' && piece != 'p') continue;
                boolean isolated = true;
                for (int r = 0; r < 8; r++) {
                    if (piece == 'P') {
                        if (col > 0 && board.board[r][col - 1] == 'P') isolated = false;
                        if (col < 7 && board.board[r][col + 1] == 'P') isolated = false;
                    }
                    else {
                        if (col > 0 && board.board[r][col - 1] == 'p') isolated = false;
                        if (col < 7 && board.board[r][col + 1] == 'p') isolated = false;
                    }
                }
                if (isolated) {
                    if (piece == 'P') score -= 15;
                    else score += 15;
                }
            }
        }
        return score;
    }

    private static boolean isPassedPawn(Board board, int row, int col, char pawn) {

        if (Character.isUpperCase(pawn)) {

            // Check all squares ahead
            for (int r = row - 1; r >= 0; r--) {

                // Left file
                if (col > 0 && board.board[r][col - 1] == 'p')
                    return false;

                // Same file
                if (board.board[r][col] == 'p')
                    return false;

                // Right file
                if (col < 7 && board.board[r][col + 1] == 'p')
                    return false;
            }

        } else {

            // Check all squares ahead
            for (int r = row + 1; r < 8; r++) {

                // Left file
                if (col > 0 && board.board[r][col - 1] == 'P')
                    return false;

                // Same file
                if (board.board[r][col] == 'P')
                    return false;

                // Right file
                if (col < 7 && board.board[r][col + 1] == 'P')
                    return false;
            }

        }

        return true;
    }

    private static int doubledPawns(Board board) {int score = 0;
        for (int col = 0; col < 8; col++) {
            int white = 0;
            int black = 0;
            for (int row = 0; row < 8; row++) {
                if (board.board[row][col] == 'P') white++;
                else if (board.board[row][col] == 'p') black++;
            }
            if (white > 1) score -= 20 * (white - 1);
            if (black > 1) score += 20 * (black - 1);
        }
        return score;
    }

    private static int mobility(Board board) {
        int whiteMoves = MoveGenerator.generateLegalMoves(board, true).size();
        int blackMoves = MoveGenerator.generateLegalMoves(board, false).size();
        return (whiteMoves - blackMoves) * 5;
    }

    private static int bishopPair(Board board) {
        int whiteBishops = 0;
        int blackBishops = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board.board[row][col];
                if (piece == 'B') whiteBishops++;
                else if (piece == 'b') blackBishops++;
            }
        }
        int score = 0;
        if (whiteBishops >= 2) score += 30;
        if (blackBishops >= 2) score -= 30;
        return score;
    }

    private static int material(Board board) {
        int score = 0;
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                char piece = board.board[row][col];
                if(piece == '.') continue;
                int value = getPieceValue(piece);
                if(Character.isUpperCase(piece)) score += value;
                else score -= value;
            }
        }
        return score;
    }
    private static int pieceSquareEvaluation(Board board) {
        int score = 0;
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                char piece = board.board[row][col];
                if(piece == '.') continue;
                int bonus = pieceSquareBonus(piece, row, col);
                if(Character.isUpperCase(piece)) score += bonus;
                else score -= bonus;
            }
        }
        return score;
    }
    private static int pieceSquareBonus(char piece, int row, int col) {
        switch (Character.toLowerCase(piece)) {
            case 'p': return getTableValue(PAWN_TABLE, piece, row, col);
            case 'n': return getTableValue(KNIGHT_TABLE, piece, row, col);
            case 'b': return getTableValue(BISHOP_TABLE, piece, row, col);
            case 'r': return getTableValue(ROOK_TABLE, piece, row, col);
            case 'q': return getTableValue(QUEEN_TABLE, piece, row, col);
            case 'k': return getTableValue(KING_MIDDLE_GAME_TABLE, piece, row, col);
            default: return 0;
        }
    }
    public static int getPieceValue(char piece) {
        switch(Character.toLowerCase(piece)) {
            case 'p': return 100;
            case 'n': return 320;
            case 'b': return 330;
            case 'r': return 500;
            case 'q': return 900;
            case 'k': return 20000;
            default: return 0;
        }
    }

    private static int pawnShield(Board board) {
        int score = 0;

        int whiteKingRow = -1;
        int whiteKingCol = -1;

        int blackKingRow = -1;
        int blackKingCol = -1;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                if(board.board[row][col] == 'K') {
                    whiteKingRow = row;
                    whiteKingCol = col;

                }
                else if(board.board[row][col] == 'k') {
                    blackKingRow = row;
                    blackKingCol = col;
                }
            }
        }
        score += evaluatePawnShield(board, whiteKingRow, whiteKingCol, true);
        score -= evaluatePawnShield(board, blackKingRow, blackKingCol, false);
        return score;
    }

    private static int evaluatePawnShield(Board board, int kingRow, int kingCol, boolean white) {
        int score = 0;
        if(kingRow == -1) return 0;
        int pawnRow;
        if(white) pawnRow = kingRow - 1;
        else pawnRow = kingRow + 1;
        if(pawnRow < 0 || pawnRow >= 8) return 0;
        for(int col = kingCol - 1;
            col <= kingCol + 1;
            col++) {
            if(col < 0 || col >= 8) continue;
            char expectedPawn = white ? 'P' : 'p';
            if(board.board[pawnRow][col] == expectedPawn) score += 10;
        }
        return score;
    }

    public static class MaterialEvaluator {
    }
}