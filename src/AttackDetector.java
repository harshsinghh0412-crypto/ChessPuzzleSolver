public class AttackDetector {
    public static int[] findKing(Board board, boolean whiteKing) {
        char king = whiteKing ? 'K' : 'k';
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(board.board[row][col]==king) return new int[]{row,col};
            }
        }
        return null;
    }
    public static boolean isKingInCheck(Board board, boolean whiteKing) {
        int[] kingPosition = findKing(board, whiteKing);
        if(kingPosition == null) return false;
        return isSquareAttacked(board, kingPosition[0], kingPosition[1], !whiteKing);
    }
    public static boolean isSquareAttacked(Board board, int row, int col, boolean byWhite) {

        // Knight attacks
        int[] dr = {-2,-2,-1,-1,1,1,2,2};
        int[] dc = {-1,1,-2,2,-2,2,-1,1};

        char attacker = byWhite ? 'N' : 'n';

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];
            if (r < 0 || r >= 8 || c < 0 || c >= 8) continue;
            if (board.board[r][c] == attacker) return true;
        }
        // Pawn attacks
        if (byWhite) {
            if (row < 7 && col > 0 && board.board[row + 1][col - 1] == 'P') return true;
            if (row < 7 && col < 7 && board.board[row + 1][col + 1] == 'P') return true;
        }
        else {
            if (row > 0 && col > 0 && board.board[row - 1][col - 1] == 'p') return true;
            if (row > 0 && col < 7 && board.board[row - 1][col + 1] == 'p') return true;
        }

        // King attacks
        int[] kingRow = {-1,-1,-1,0,0,1,1,1};
        int[] kingCol = {-1,0,1,-1,1,-1,0,1};

        char king = byWhite ? 'K' : 'k';

        for (int i = 0; i < 8; i++) {
            int r = row + kingRow[i];
            int c = col + kingCol[i];
            if (r < 0 || r >= 8 || c < 0 || c >= 8) continue;
            if (board.board[r][c] == king) return true;
        }

        // Diagonal attacks

        int[] bishopRow = {-1, -1, 1, 1};
        int[] bishopCol = {-1, 1, -1, 1};

        char bishop = byWhite ? 'B' : 'b';
        char queen  = byWhite ? 'Q' : 'q';

        for (int dir = 0; dir < 4; dir++) {
            int r = row + bishopRow[dir];
            int c = col + bishopCol[dir];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                char piece = board.board[r][c];
                if (piece == '.') {
                    r += bishopRow[dir];
                    c += bishopCol[dir];
                    continue;
                }
                if (piece == bishop || piece == queen) return true;
                break;
            }
        }

        // Straight attacks

        int[] rookRow = {-1, 1, 0, 0};
        int[] rookCol = {0, 0, -1, 1};

        char rook = byWhite ? 'R' : 'r';
        char queenStraight = byWhite ? 'Q' : 'q';

        for (int dir = 0; dir < 4; dir++) {
            int r = row + rookRow[dir];
            int c = col + rookCol[dir];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                char piece = board.board[r][c];
                if (piece == '.') {
                    r += rookRow[dir];
                    c += rookCol[dir];
                    continue;
                }
                if (piece == rook || piece == queenStraight) return true;
                break;
            }
        }
        return false;
    }
}
