public class Search {
    public static Move search(Board board, boolean whiteToMove, int maxDepth) {
        Move bestMove = null;
        TranspositionTable.clear();
        KillerMoves.clear();
        for (int depth = 1; depth <= maxDepth; depth++) {
            System.out.println("------------------------------------");
            System.out.println("Searching Depth : " + depth);
            long startTime = System.currentTimeMillis();
            bestMove = Minimax.findBestMove(board, depth, whiteToMove);
            long endTime = System.currentTimeMillis();
            System.out.println("Best Move      : " + bestMove);
            System.out.println("Nodes Searched : " + Minimax.nodes);
            System.out.println("Time Taken     : " + (endTime - startTime) + " ms");
            System.out.println();
        }
        System.out.println("------------------------------------");
        System.out.println("Final Best Move : " + bestMove);
        return bestMove;
    }

}