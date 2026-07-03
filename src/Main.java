import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Zobrist.initialize();
        Board board = new Board();
        String fen = "r3rk2/2pb1ppp/1pn1qn2/4p3/3pP3/P4NP1/1PPN1PBP/1KR1Q2R b - - 0 1";
        FenParser.parse(fen, board);
        Move best = Search.search(board, board.whiteToMove, 5);
//        board.printBoard();
//        ArrayList<Move> moves = MoveGenerator.generateMoves(board,true);
//        for(Move move : moves) System.out.println(move);
    }
}