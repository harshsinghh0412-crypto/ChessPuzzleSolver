import java.util.ArrayList;

public class Board {

    char[][] board;

    public boolean whiteToMove = true;
    public long zobristHash;
    public ArrayList<Long> positionHistory = new ArrayList<>();

    // Castling rights+
    public boolean whiteKingSideCastle = true;
    public boolean whiteQueenSideCastle = true;

    public boolean blackKingSideCastle = true;
    public boolean blackQueenSideCastle = true;

    public int enPassantRow = -1;
    public int enPassantCol = -1;

    public boolean isThreefoldRepetition() {
        int count = 0;
        for (long hash : positionHistory) {
            if (hash == zobristHash) {
                count++;
            }
        }

        return count >= 3;

    }

    public Board() {
        board = new char[8][8];
    }

    public void printBoard() {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public void makeMove(Move move) {

        move.movedPiece = board[move.fromRow][move.fromCol];
        move.capturedPiece = board[move.toRow][move.toCol];

        move.prevEnPassantRow = enPassantRow;
        move.prevEnPassantCol = enPassantCol;
        enPassantRow = -1;
        enPassantCol = -1;

        if (move.capturedPiece == 'R') {
            if (move.toRow == 7 && move.toCol == 0) {
                whiteQueenSideCastle = false;
            }
            if (move.toRow == 7 && move.toCol == 7) {
                whiteKingSideCastle = false;
            }
        }
        if (move.capturedPiece == 'r') {
            if (move.toRow == 0 && move.toCol == 0) {
                blackQueenSideCastle = false;
            }
            if (move.toRow == 0 && move.toCol == 7) {
                blackKingSideCastle = false;
            }
        }

        move.prevWhiteKingSideCastle = whiteKingSideCastle;
        move.prevWhiteQueenSideCastle = whiteQueenSideCastle;

        move.prevBlackKingSideCastle = blackKingSideCastle;
        move.prevBlackQueenSideCastle = blackQueenSideCastle;

        Zobrist.removePiece(this, move.movedPiece, move.fromRow, move.fromCol);

        if (move.capturedPiece != '.' && !move.isEnPassant) {
            Zobrist.removePiece(this, move.capturedPiece, move.toRow, move.toCol);
        }

        if(move.isPromotion){
            board[move.toRow][move.toCol] = move.promotionPiece;
        }
        else{
            board[move.toRow][move.toCol] = move.movedPiece;
        }
        board[move.fromRow][move.fromCol] = '.';

        if (move.isEnPassant) {
            if (move.movedPiece == 'P') {
                move.capturedPiece = board[move.toRow + 1][move.toCol];
                Zobrist.removePiece(this, 'p', move.toRow + 1, move.toCol);
                board[move.toRow + 1][move.toCol] = '.';
            }
            else {
                move.capturedPiece = board[move.toRow - 1][move.toCol];
                Zobrist.removePiece(this, 'P', move.toRow - 1, move.toCol);
                board[move.toRow - 1][move.toCol] = '.';
            }
        }
        if (Character.toLowerCase(move.movedPiece) == 'p') {
            if (Math.abs(move.toRow - move.fromRow) == 2) {
                enPassantRow = (move.fromRow + move.toRow) / 2;
                enPassantCol = move.fromCol;
            }
        }

        if (move.isCastle) {
            if (move.movedPiece == 'K') {
                if (move.isKingSideCastle) {
                    Zobrist.removePiece(this, 'R', 7, 7);
                    board[7][5] = 'R';
                    board[7][7] = '.';
                    Zobrist.addPiece(this, 'R', 7, 5);
                } else {
                    Zobrist.removePiece(this, 'R', 7, 0);
                    board[7][3] = 'R';
                    board[7][0] = '.';
                    Zobrist.addPiece(this, 'R', 7, 3);
                }
            } else {
                if (move.isKingSideCastle) {
                    Zobrist.removePiece(this, 'r', 0, 7);
                    board[0][5] = 'r';
                    board[0][7] = '.';
                    Zobrist.addPiece(this, 'r', 0, 5);
                } else {
                    Zobrist.removePiece(this, 'r', 0, 0);
                    board[0][3] = 'r';
                    board[0][0] = '.';
                    Zobrist.addPiece(this, 'r', 0, 3);
                }
            }
        }

        if(move.isPromotion){
            Zobrist.addPiece(this, move.promotionPiece, move.toRow, move.toCol);
        }
        else{
            Zobrist.addPiece(this, move.movedPiece, move.toRow, move.toCol);
        }

        if (move.movedPiece == 'K') {
            whiteKingSideCastle = false;
            whiteQueenSideCastle = false;
        }
        if (move.movedPiece == 'k') {
            blackKingSideCastle = false;
            blackQueenSideCastle = false;
        }
        if (move.movedPiece == 'R') {
            if (move.fromRow == 7 && move.fromCol == 0) whiteQueenSideCastle = false;
            if (move.fromRow == 7 && move.fromCol == 7) whiteKingSideCastle = false;
        }
        if (move.movedPiece == 'r') {
            if (move.fromRow == 0 && move.fromCol == 0) blackQueenSideCastle = false;
            if (move.fromRow == 0 && move.fromCol == 7) blackKingSideCastle = false;
        }
        positionHistory.add(zobristHash);
        whiteToMove = !whiteToMove;
        zobristHash ^= Zobrist.sideToMoveKey;
    }

    public void undoMove(Move move) {
        if(move.isPromotion){
            Zobrist.removePiece(this, move.promotionPiece, move.toRow, move.toCol);
        }
        else{
            Zobrist.removePiece(this, move.movedPiece, move.toRow, move.toCol);
        }
        board[move.fromRow][move.fromCol] = move.movedPiece;
        board[move.toRow][move.toCol] = move.capturedPiece;

        // Undo En Passant
        if (move.isEnPassant) {
            board[move.toRow][move.toCol] = '.';
            if (move.movedPiece == 'P') {
                board[move.toRow + 1][move.toCol] = 'p';
                Zobrist.addPiece(this, 'p', move.toRow + 1, move.toCol);
            }
            else {
                board[move.toRow - 1][move.toCol] = 'P';
                Zobrist.addPiece(this, 'P', move.toRow - 1, move.toCol);
            }
        }

        if (move.isCastle) {
            if (move.movedPiece == 'K') {
                if (move.isKingSideCastle) {
                    Zobrist.removePiece(this, 'R', 7, 5);
                    board[7][7] = 'R';
                    board[7][5] = '.';
                    Zobrist.addPiece(this, 'R', 7, 7);
                }
                else {
                    Zobrist.removePiece(this, 'R', 7, 3);
                    board[7][0] = 'R';
                    board[7][3] = '.';
                    Zobrist.addPiece(this, 'R', 7, 0);
                }
            }
            else {
                if (move.isKingSideCastle) {
                    Zobrist.removePiece(this, 'r', 0, 5);
                    board[0][7] = 'r';
                    board[0][5] = '.';
                    Zobrist.addPiece(this, 'r', 0, 7);
                }
                else {
                    Zobrist.removePiece(this, 'r', 0, 3);
                    board[0][0] = 'r';
                    board[0][3] = '.';
                    Zobrist.addPiece(this, 'r', 0, 0);
                }
            }
        }
        Zobrist.addPiece(this, move.movedPiece, move.fromRow, move.fromCol);
        if (move.capturedPiece != '.' && !move.isEnPassant) {
            Zobrist.addPiece(this, move.capturedPiece, move.toRow, move.toCol);
        }

        whiteKingSideCastle = move.prevWhiteKingSideCastle;
        whiteQueenSideCastle = move.prevWhiteQueenSideCastle;

        blackKingSideCastle = move.prevBlackKingSideCastle;
        blackQueenSideCastle = move.prevBlackQueenSideCastle;

        enPassantRow = move.prevEnPassantRow;
        enPassantCol = move.prevEnPassantCol;

        positionHistory.remove(positionHistory.size() - 1);
        whiteToMove = !whiteToMove;
        zobristHash ^= Zobrist.sideToMoveKey;
    }
    public boolean isInsufficientMaterial() {

        int whiteBishops = 0;
        int blackBishops = 0;

        int whiteKnights = 0;
        int blackKnights = 0;

        int whiteOthers = 0;
        int blackOthers = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board[row][col];
                switch (piece) {
                    case '.': break;
                    case 'P':
                    case 'R':
                    case 'Q':
                        whiteOthers++;
                        break;
                    case 'p':
                    case 'r':
                    case 'q':
                        blackOthers++;
                        break;
                    case 'B':
                        whiteBishops++;
                        break;
                    case 'b':
                        blackBishops++;
                        break;
                    case 'N':
                        whiteKnights++;
                        break;
                    case 'n':
                        blackKnights++;
                        break;
                }
            }
        }

        if (whiteOthers > 0 || blackOthers > 0) return false;

        int whiteMinor = whiteBishops + whiteKnights;
        int blackMinor = blackBishops + blackKnights;

        if (whiteMinor == 0 && blackMinor == 0) return true;
        if (whiteMinor == 1 && blackMinor == 0 && whiteBishops == 1) return true;
        if (whiteMinor == 1 && blackMinor == 0 && whiteKnights == 1) return true;
        if (whiteMinor == 0 && blackMinor == 1 && blackBishops == 1) return true;
        if (whiteMinor == 0 && blackMinor == 1 && blackKnights == 1) return true;
        return false;
    }
}