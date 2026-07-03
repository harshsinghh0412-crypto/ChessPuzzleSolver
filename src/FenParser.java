public class FenParser {
    public static void parse(String fen, Board board) {
        String[] parts = fen.split(" ");
        String boardPosition = parts[0];
        String[] rows = boardPosition.split("/");
        board.whiteToMove = parts[1].equals("w");
        // Reset castling rights
        board.whiteKingSideCastle = false;
        board.whiteQueenSideCastle = false;
        board.blackKingSideCastle = false;
        board.blackQueenSideCastle = false;

        String castling = parts[2];

        if (castling.contains("K")) {
            board.whiteKingSideCastle = true;
        }

        if (castling.contains("Q")) {
            board.whiteQueenSideCastle = true;
        }

        if (castling.contains("k")) {
            board.blackKingSideCastle = true;
        }

        if (castling.contains("q")) {
            board.blackQueenSideCastle = true;
        }

        board.enPassantRow = -1;
        board.enPassantCol = -1;

        String ep = parts[3];

        if (!ep.equals("-")) {
            board.enPassantCol = ep.charAt(0) - 'a';
            int rank = ep.charAt(1) - '0';
            board.enPassantRow = 8 - rank;
        }

        for(int i = 0; i < 8; i++) {
            int col = 0;
            for(char ch : rows[i].toCharArray()) {
                if(Character.isDigit(ch)) {
                    int dots = ch - '0';
                    for(int j = 0; j < dots; j++) {
                        board.board[i][col] = '.';
                        col++;
                    }
                }
                else {
                    board.board[i][col] = ch;
                    col++;
                }
            }
        }
        board.zobristHash = Zobrist.computeHash(board);
        board.positionHistory.clear();
        board.positionHistory.add(board.zobristHash);
    }
}