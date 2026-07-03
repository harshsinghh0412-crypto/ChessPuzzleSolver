# ChessPuzzleSolver

A Java-based chess engine capable of solving chess positions using modern search techniques and board evaluation heuristics.

## Features

### Search Algorithm
- Alpha-Beta Pruning
- Iterative Deepening Search
- Quiescence Search
- Move Ordering (MVV-LVA)
- Killer Move Heuristic
- Transposition Table
- Zobrist Hashing

### Move Generation
- Legal Move Generation
- Check Detection
- Checkmate Detection
- Stalemate Detection
- Castling
- En Passant
- Pawn Promotion

### Draw Detection
- Threefold Repetition
- Fifty-Move Rule
- Insufficient Material

### Evaluation Function
- Material Evaluation
- Piece-Square Tables
- Mobility Evaluation
- Pawn Structure Evaluation
  - Passed Pawns
  - Doubled Pawns
  - Isolated Pawns
- Bishop Pair Bonus
- King Safety
  - Pawn Shield

## Technologies Used

- Java
- IntelliJ IDEA
- Git
- GitHub

## Project Structure

```
src/
├── AttackDetector.java
├── Board.java
├── Evaluator.java
├── FenParser.java
├── Main.java
├── Minimax.java
├── Move.java
├── MoveGenerator.java
├── MoveOrdering.java
├── Search.java
├── Solver.java
├── TranspositionTable.java
├── TTEntry.java
├── UndoInfo.java
└── Zobrist.java
```

## Running the Project

1. Clone the repository

```bash
git clone https://github.com/harshsinghh0412-crypto/ChessPuzzleSolver.git
```

2. Open the project in IntelliJ IDEA.

3. Run `Main.java`.

4. Provide a FEN position in `Main.java`.

Example:

```java
String fen = "r3rk2/2pb1ppp/1pn1qn2/4p3/3pP3/P4NP1/1PPN1PBP/1KR1Q2R b - - 0 1";
```

5. The engine searches the position and prints the best move along with search statistics.

## Example Output

```
Searching Depth : 5

Best Move      : e6e5
Nodes Searched : 3248570
Time Taken     : 421 ms
```

## Future Improvements

- UCI Protocol Support
- Opening Book
- Endgame Tablebases
- Null Move Pruning
- History Heuristic
- Principal Variation Search (PVS)
- Late Move Reductions (LMR)
- Aspiration Windows
- Multi-threaded Search

## Author

**Harsh Singh**

GitHub: https://github.com/harshsinghh0412-crypto
