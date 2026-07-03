import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Move {
    public int fromRow;
    public int fromCol;

    public int toRow;
    public int toCol;
    public char movedPiece = '.';

    public boolean prevWhiteKingSideCastle;
    public boolean prevWhiteQueenSideCastle;

    public boolean prevBlackKingSideCastle;
    public boolean prevBlackQueenSideCastle;

    public boolean isCastle = false;
    public boolean isKingSideCastle = false;

    public boolean isPromotion = false;
    public char promotionPiece = '.';

    public boolean isEnPassant = false;
    public int prevEnPassantRow = -1;
    public int prevEnPassantCol = -1;

    public char capturedPiece;

    int score;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    @Override
    public String toString() {
        char fromFile = (char) ('a' + fromCol);
        int fromRank = 8 - fromRow;
        char toFile = (char) ('a' + toCol);
        int toRank = 8 - toRow;
        return "" + fromFile + fromRank + " -> " + toFile + toRank;
    }
}