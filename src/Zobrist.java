import java.util.Random;

import static java.util.Objects.hash;

public class Zobrist {
    public static long[][][] keys = new long[12][8][8];
    public static long sideToMoveKey;

    public static long[] castleKeys = new long[4];
    public static long[] enPassantKeys = new long[8];
    public static void initialize() {
        Random random = new Random(42);
        for(int piece = 0; piece < 12; piece++) {
            for(int row = 0; row < 8; row++) {
                for(int col = 0; col < 8; col++) {
                    keys[piece][row][col] = random.nextLong();
                }
            }
        }
        sideToMoveKey = random.nextLong();
        for (int i = 0; i < 4; i++) {
            castleKeys[i] = random.nextLong();
        }
        for (int i = 0; i < 8; i++) {
            enPassantKeys[i] = random.nextLong();
        }
    }
    private static int pieceIndex(char piece) {
        switch(piece) {
            case 'P': return 0;
            case 'N': return 1;
            case 'B': return 2;
            case 'R': return 3;
            case 'Q': return 4;
            case 'K': return 5;

            case 'p': return 6;
            case 'n': return 7;
            case 'b': return 8;
            case 'r': return 9;
            case 'q': return 10;
            case 'k': return 11;
        }
        return -1;
    }
    public static long computeHash(Board board) {
        long hash = hash(board);
        if (!board.whiteToMove) {
            hash ^= sideToMoveKey;
        }
        if (board.whiteKingSideCastle) hash ^= castleKeys[0];
        if (board.whiteQueenSideCastle) hash ^= castleKeys[1];
        if (board.blackKingSideCastle) hash ^= castleKeys[2];
        if (board.blackQueenSideCastle) hash ^= castleKeys[3];
        if (board.enPassantCol != -1) {
            hash ^= enPassantKeys[board.enPassantCol];
        }
        return hash;
    }
    public static void removePiece(Board board, char piece, int row, int col) {
        int index = pieceIndex(piece);
        if (index == -1) return;
        board.zobristHash ^= keys[index][row][col];
    }
    public static void addPiece(Board board, char piece, int row, int col) {
        int index = pieceIndex(piece);
        if (index == -1) return;
        board.zobristHash ^= keys[index][row][col];
    }
}