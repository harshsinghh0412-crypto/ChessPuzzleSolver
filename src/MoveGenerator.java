import java.util.ArrayList;

public class MoveGenerator {

    public static ArrayList<Move> generateMoves(Board board, boolean whiteToMove) {

        ArrayList<Move> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                char piece = board.board[row][col];

                if (piece == '.') continue;
                if (whiteToMove && Character.isLowerCase(piece)) continue;
                if (!whiteToMove && Character.isUpperCase(piece)) continue;

                switch (Character.toLowerCase(piece)) {

                    case 'p':
                        generatePawnMoves(board, row, col, moves);
                        break;

                    case 'n':
                        generateKnightMoves(board, row, col, moves);
                        break;

                    case 'b':
                        generateBishopMoves(board, row, col, moves);
                        break;

                    case 'r':
                        generateRookMoves(board, row, col, moves);
                        break;

                    case 'q':
                        generateQueenMoves(board, row, col, moves);
                        break;

                    case 'k':
                        generateKingMoves(board, row, col, moves);
                        break;
                }
            }
        }

        return moves;
    }

    private static void generatePawnMoves(Board board, int row, int col, ArrayList<Move> moves) {
        char piece = board.board[row][col];

        if (Character.isUpperCase(piece)) {
            if (row > 0 && board.board[row - 1][col] == '.') {
                if (row == 1) {
                    Move move = new Move(row, col, row - 1, col);
                    move.isPromotion = true;
                    move.promotionPiece = 'Q';
                    moves.add(move);
                }
                else {
                    moves.add(new Move(row, col, row - 1, col));
                }
                if (row == 6 && board.board[row - 2][col] == '.') {
                    moves.add(new Move(row, col, row - 2, col));
                }
            }
            if (row > 0 && col > 0 && Character.isLowerCase(board.board[row - 1][col - 1])) {
                if(row == 1){
                    Move move = new Move(row,col,row-1,col-1);
                    move.isPromotion = true;
                    move.promotionPiece = 'Q';
                    moves.add(move);
                }
                else{
                    moves.add(new Move(row,col,row-1,col-1));
                }
            }
            if (row > 0 && col < 7 && Character.isLowerCase(board.board[row - 1][col + 1])) {
                if(row == 1) {
                    Move move = new Move(row, col, row - 1, col + 1);
                    move.isPromotion = true;
                    move.promotionPiece = 'Q';
                    moves.add(move);
                }
                else {
                    moves.add(new Move(row, col, row - 1, col + 1));
                }
            }
            // White En Passant
            if (row == 3) {
                if (col > 0 && board.enPassantRow == 2 && board.enPassantCol == col - 1) {
                    Move move = new Move(row, col, 2, col - 1);
                    move.isEnPassant = true;
                    moves.add(move);
                }
                if (col < 7 && board.enPassantRow == 2 && board.enPassantCol == col + 1) {
                    Move move = new Move(row, col, 2, col + 1);
                    move.isEnPassant = true;
                    moves.add(move);
                }
            }
        }
        else {
            if (row < 7 && board.board[row + 1][col] == '.') {
                if(row == 6) {
                    Move move = new Move(row, col, row + 1, col);
                    move.isPromotion = true;
                    move.promotionPiece = 'q';
                    moves.add(move);
                }
                else moves.add(new Move(row, col, row + 1, col));

                if (row == 1 && board.board[row + 2][col] == '.') {
                    moves.add(new Move(row, col, row + 2, col));
                }
            }
            if (row < 7 && col > 0 && Character.isUpperCase(board.board[row + 1][col - 1])) {
                if(row == 6) {
                    Move move = new Move(row, col, row + 1, col - 1);
                    move.isPromotion = true;
                    move.promotionPiece = 'q';
                    moves.add(move);
                }
                else moves.add(new Move(row, col, row + 1, col - 1));
            }
            if (row < 7 && col < 7 && Character.isUpperCase(board.board[row + 1][col + 1])) {
                if(row == 6) {
                    Move move = new Move(row, col, row + 1, col + 1);
                    move.isPromotion = true;
                    move.promotionPiece = 'q';
                    moves.add(move);
                }
                else moves.add(new Move(row, col, row + 1, col + 1));
            }
            // Black En Passant
            if (row == 4) {
                if (col > 0 && board.enPassantRow == 5 && board.enPassantCol == col - 1) {
                    Move move = new Move(row, col, 5, col - 1);
                    move.isEnPassant = true;
                    moves.add(move);
                }
                if (col < 7 && board.enPassantRow == 5 && board.enPassantCol == col + 1) {
                    Move move = new Move(row, col, 5, col + 1);
                    move.isEnPassant = true;
                    moves.add(move);
                }
            }
        }
    }

    private static void generateKnightMoves(Board board, int row, int col, ArrayList<Move> moves) {
        int[] dr = {-2,-2,-1,-1,1,1,2,2};
        int[] dc = {-1,1,-2,2,-2,2,-1,1};

        char knight = board.board[row][col];

        for(int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];

            if(newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) continue;

            char target = board.board[newRow][newCol];
            if(target == '.') {
                moves.add(new Move(row,col,newRow,newCol));
            }

            else if(Character.isUpperCase(knight) && Character.isLowerCase(target)) {
                moves.add(new Move(row,col,newRow,newCol));
            }
            else if(Character.isLowerCase(knight)  && Character.isUpperCase(target)) {
                moves.add(new Move(row,col,newRow,newCol));
            }
        }
    }

    private static void generateBishopMoves(Board board, int row, int col, ArrayList<Move> moves) {
        int[] dr = {-1, -1, 1, 1};
        int[] dc = {-1, 1, -1, 1};

        char bishop = board.board[row][col];

        for (int dir = 0; dir < 4; dir++) {

            int newRow = row + dr[dir];
            int newCol = col + dc[dir];

            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                char target = board.board[newRow][newCol];
                if (target == '.') {
                    moves.add(new Move(row, col, newRow, newCol));
                }
                else {
                    if (Character.isUpperCase(bishop) && Character.isLowerCase(target)) {
                        moves.add(new Move(row, col, newRow, newCol));
                    }
                    else if (Character.isLowerCase(bishop) && Character.isUpperCase(target)) {
                        moves.add(new Move(row, col, newRow, newCol));
                    }
                    break;
                }
                newRow += dr[dir];
                newCol += dc[dir];
            }
        }
    }

    private static void generateRookMoves(Board board, int row, int col, ArrayList<Move> moves) {
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        char rook = board.board[row][col];

        for (int dir = 0; dir < 4; dir++) {
            int newRow = row + dr[dir];
            int newCol = col + dc[dir];
            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                char target = board.board[newRow][newCol];
                if (target == '.') {
                    moves.add(new Move(row, col, newRow, newCol));
                }
                else {
                    if (Character.isUpperCase(rook) && Character.isLowerCase(target)) {
                        moves.add(new Move(row, col, newRow, newCol));
                    }
                    else if (Character.isLowerCase(rook) && Character.isUpperCase(target)) {
                        moves.add(new Move(row, col, newRow, newCol));
                    }
                    break;
                }
                newRow += dr[dir];
                newCol += dc[dir];
            }
        }
    }

    private static void generateQueenMoves(Board board, int row, int col, ArrayList<Move> moves) {
        generateBishopMoves(board, row, col, moves);
        generateRookMoves(board, row, col, moves);
    }

    private static void generateKingMoves(Board board, int row, int col, ArrayList<Move> moves) {
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};

        char king = board.board[row][col];

        for(int i = 0; i < 8; i++) {
            int newRow = row + dr[i];
            int newCol = col + dc[i];
            if(newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) continue;
            char target = board.board[newRow][newCol];
            if(target == '.') {
                moves.add(new Move(row,col,newRow,newCol));
            }
            else if(Character.isUpperCase(king) && Character.isLowerCase(target)) {
                moves.add(new Move(row,col,newRow,newCol));
            }
            else if(Character.isLowerCase(king) && Character.isUpperCase(target)) {
                moves.add(new Move(row,col,newRow,newCol));
            }
        }
        generateCastlingMoves(board, moves, row, col, Character.isUpperCase(king));
    }

    private static void generateCastlingMoves(Board board, ArrayList<Move> moves, int row, int col, boolean whiteToMove) {
        if (whiteToMove) {
            generateWhiteCastling(board, moves);
        } else {
            generateBlackCastling(board, moves);
        }
    }

    private static void generateWhiteCastling(Board board, ArrayList<Move> moves) {
        if (!board.whiteKingSideCastle) return;
        // King and rook must be on starting squares
        if (board.board[7][4] != 'K') return;
        if (board.board[7][7] != 'R') return;

        if (board.board[7][5] != '.') return;
        if (board.board[7][6] != '.') return;

        if (AttackDetector.isSquareAttacked(board, 7, 4, false)) return;
        if (AttackDetector.isSquareAttacked(board, 7, 5, false)) return;
        if (AttackDetector.isSquareAttacked(board, 7, 6, false)) return;

        Move move = new Move(7, 4, 7, 6);
        move.isCastle = true;
        move.isKingSideCastle = true;
        moves.add(move);

        generateWhiteQueenSide(board, moves);
    }

    private static void generateWhiteQueenSide(Board board, ArrayList<Move> moves) {
        if (!board.whiteQueenSideCastle) return;

        // King and rook must be on starting squares
        if (board.board[7][4] != 'K') return;
        if (board.board[7][0] != 'R') return;

        if (board.board[7][1] != '.') return;
        if (board.board[7][2] != '.') return;
        if (board.board[7][3] != '.') return;

        if (AttackDetector.isSquareAttacked(board, 7, 4, false)) return;
        if (AttackDetector.isSquareAttacked(board, 7, 3, false)) return;
        if (AttackDetector.isSquareAttacked(board, 7, 2, false)) return;

        Move move = new Move(7, 4, 7, 2);
        move.isCastle = true;
        move.isKingSideCastle = false;
        moves.add(move);
    }

    private static void generateBlackCastling(Board board, ArrayList<Move> moves) {
        if (!board.blackKingSideCastle) return;

        // King and rook must be on starting squares
        if (board.board[0][4] != 'k') return;
        if (board.board[0][7] != 'r') return;

        if (board.board[0][5] != '.') return;
        if (board.board[0][6] != '.') return;

        if (AttackDetector.isSquareAttacked(board, 0, 4, true)) return;
        if (AttackDetector.isSquareAttacked(board, 0, 5, true)) return;
        if (AttackDetector.isSquareAttacked(board, 0, 6, true)) return;

        Move move = new Move(0, 4, 0, 6);
        move.isCastle = true;
        move.isKingSideCastle = true;
        moves.add(move);

        generateBlackQueenSide(board, moves);
    }

    private static void generateBlackQueenSide(Board board, ArrayList<Move> moves) {
        if (!board.blackQueenSideCastle) return;

        // King and rook must be on starting squares
        if (board.board[0][4] != 'k') return;
        if (board.board[0][0] != 'r') return;

        if (board.board[0][1] != '.') return;
        if (board.board[0][2] != '.') return;
        if (board.board[0][3] != '.') return;

        if (AttackDetector.isSquareAttacked(board, 0, 4, true)) return;
        if (AttackDetector.isSquareAttacked(board, 0, 3, true)) return;
        if (AttackDetector.isSquareAttacked(board, 0, 2, true)) return;

        Move move = new Move(0, 4, 0, 2);
        move.isCastle = true;
        move.isKingSideCastle = false;
        moves.add(move);
    }

    public static ArrayList<Move> generateLegalMoves(Board board, boolean whiteToMove) {
        ArrayList<Move> pseudoMoves = generateMoves(board, whiteToMove);
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoMoves) {
            board.makeMove(move);
            if (!AttackDetector.isKingInCheck(board, whiteToMove)) {
                legalMoves.add(move);
            }
            board.undoMove(move);
        }
        return legalMoves;
    }

    public static ArrayList<Move> generateCaptureMoves(Board board, boolean whiteToMove) {
        ArrayList<Move> legalMoves = generateLegalMoves(board, whiteToMove);
        ArrayList<Move> captures = new ArrayList<>();
        for (Move move : legalMoves) {
            if (move.capturedPiece != '.' || move.isEnPassant) {
                captures.add(move);
            }
        }
        return captures;
    }
}