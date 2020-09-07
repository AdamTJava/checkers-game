import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckersData {

    static final int
            EMPTY = 0,
            RED = 1,
            RED_QUEEN = 2,
            BLACK = 3,
            BLACK_QUEEN = 4;

    public static int[][] board;
    Map<Integer, Integer> queenMoveFields;

    public CheckersData() {
        board = new int[8][8];
    }

    public void prepareGameData() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 != col % 2) {
                    if (row < 3) {
                        board[row][col] = RED;
                    } else if (row > 4) {
                        board[row][col] = BLACK;
                    } else {
                        board[row][col] = EMPTY;
                    }

                }
            }
        }
    }

    public int pieceAt(int row, int col) {
        return board[row][col];
    }

    private boolean canMove(int player, int rowFrom, int colFrom, int rowTo, int colTo) {
        if (rowTo < 0 || rowTo > 7 || colTo < 0 || colTo > 7)
            return false;
        if (board[rowFrom][colFrom] == BLACK_QUEEN || board[rowFrom][colFrom] == RED_QUEEN) {
            for (Map.Entry<Integer, Integer> eachField : queenMoveFields.entrySet()) {
                if (board[eachField.getKey()][eachField.getValue()] != EMPTY) {
                    return false;
                }
            }
        }
        if (board[rowTo][colTo] != EMPTY || board[rowFrom][colFrom] == EMPTY)
            return false;
        if (player == RED) {
            if (board[rowFrom][colFrom] == RED & rowTo < rowFrom) {
                return false;
            } else {
                return true;
            }
        } else {
            if (board[rowFrom][colFrom] == BLACK & rowTo > rowFrom) {
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean canJump(int player, int rowFrom, int colFrom, int rowMiddle, int colMiddle, int rowTo, int colTo) {
        if (rowTo < 0 || rowTo > 7 || colTo < 0 || colTo > 7)
            return false;
        if (board[rowTo][colTo] != EMPTY)
            return false;
        if (player == RED) {
            if (board[rowFrom][colFrom] == RED_QUEEN) {
                for (Map.Entry<Integer, Integer> eachField : queenMoveFields.entrySet()) {
                    if (board[eachField.getKey()][eachField.getValue()] == EMPTY & (board[rowMiddle][colMiddle] == BLACK ||
                            board[rowMiddle][colMiddle] == BLACK_QUEEN)) {
                        return true;
                    }
                    if (board[eachField.getKey()][eachField.getValue()] == EMPTY & board[rowMiddle][colMiddle] == EMPTY) {
                        return false;
                    }
                    if (board[eachField.getKey()][eachField.getValue()] == RED || board[eachField.getKey()][eachField.getValue()] == RED_QUEEN ||
                            board[rowMiddle][colMiddle] == RED || board[rowMiddle][colMiddle] == RED_QUEEN) {
                        return false;
                    }
                    if ((board[eachField.getKey()][eachField.getValue()] == BLACK || board[eachField.getKey()][eachField.getValue()] == BLACK_QUEEN) &
                            (board[rowMiddle][colMiddle] == BLACK || board[rowMiddle][colMiddle] == BLACK_QUEEN)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            if (board[rowFrom][colFrom] == RED & rowTo < rowFrom) {
                return false;
            } else if (board[rowMiddle][colMiddle] != BLACK & board[rowMiddle][colMiddle] != BLACK_QUEEN) {
                return false;
            } else {
                return true;
            }
        } else {
            if (board[rowFrom][colFrom] == BLACK_QUEEN) {
                for (Map.Entry<Integer, Integer> eachField : queenMoveFields.entrySet()) {
                    if (board[eachField.getKey()][eachField.getValue()] == EMPTY & (board[rowMiddle][colMiddle] == RED ||
                            board[rowMiddle][colMiddle] == RED_QUEEN)) {
                        return true;
                    }
                    if (board[eachField.getKey()][eachField.getValue()] == EMPTY & board[rowMiddle][colMiddle] == EMPTY) {
                        return false;
                    }
                    if (board[eachField.getKey()][eachField.getValue()] == BLACK || board[eachField.getKey()][eachField.getValue()] == BLACK_QUEEN ||
                            board[rowMiddle][colMiddle] == BLACK || board[rowMiddle][colMiddle] == BLACK_QUEEN) {
                        return false;
                    }
                    if ((board[eachField.getKey()][eachField.getValue()] == RED || board[eachField.getKey()][eachField.getValue()] == RED_QUEEN) &
                            (board[rowMiddle][colMiddle] == RED || board[rowMiddle][colMiddle] == RED_QUEEN)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            if (board[rowFrom][colFrom] == BLACK & rowTo > rowFrom) {
                return false;
            } else if (board[rowMiddle][colMiddle] != RED & board[rowMiddle][colMiddle] != RED_QUEEN) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void makeMove(CheckersMove move) {

        if (board[move.fromRow][move.fromCol] != EMPTY) {
            board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
            board[move.fromRow][move.fromCol] = EMPTY;
        }

        if (move.isJump()) {
            int rowToErase = (move.fromRow + move.toRow) / 2;
            int colToErase = (move.fromCol + move.toCol) / 2;
            board[rowToErase][colToErase] = EMPTY;
        }
        if (move.toRow == 0 & board[move.toRow][move.toCol] == BLACK) {
            board[move.toRow][move.toCol] = BLACK_QUEEN;
        }
        if (move.toRow == 7 & board[move.toRow][move.toCol] == RED) {
            board[move.toRow][move.toCol] = RED_QUEEN;
        }
    }

    List<CheckersMove> getLegalMoves(int player) {

        if (player != RED & player != BLACK) {
            return null;
        }
        int playerQueen;
        if (player == RED) {
            playerQueen = RED_QUEEN;
        } else {
            playerQueen = BLACK_QUEEN;
        }

        ArrayList<CheckersMove> moveList = new ArrayList<>();
        queenMoveFields = new HashMap<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moveList.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moveList.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moveList.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moveList.add(new CheckersMove(row, col, row - 2, col - 2));
                }
                if (board[row][col] == playerQueen) {
                    for (int i = 1; i < 8; i++) {
                        for (int k = 1; k < 8; k++) {
                            if (i == k) {
                                if (row + i + 1 <= 7 & col + k + 1 <= 7)
                                    queenMoveFields.put(row + i + 1, col + k + 1);
                                if (canJump(player, row, col, row + i, col + k, row + i + 1, col + k + 1)) {
                                    moveList.add(new CheckersMove(row, col, row + i + 1, col + k + 1));
                                }
                            }
                        }
                    }
                    queenMoveFields.clear();
                    for (int i = 1; i < 8; i++) {
                        for (int k = 1; k < 8; k++) {
                            if (i == k) {
                                if (row - i - 1 >= 0 & col + k + 1 <= 7)
                                    queenMoveFields.put(row - i - 1, col + k + 1);
                                if (canJump(player, row, col, row - i, col + k, row - i - 1, col + k + 1)) {
                                    moveList.add(new CheckersMove(row, col, row - i - 1, col + k + 1));
                                }
                            }
                        }
                    }
                    queenMoveFields.clear();
                    for (int i = 1; i < 8; i++) {
                        for (int k = 1; k < 8; k++) {
                            if (i == k) {
                                if (row + i + 1 <= 7 & col - k - 1 >= 0)
                                    queenMoveFields.put(row + i + 1, col - k - 1);
                                if (canJump(player, row, col, row + i, col - k, row + i + 1, col - k - 1)) {
                                    moveList.add(new CheckersMove(row, col, row + i + 1, col - k - 1));
                                }
                            }
                        }
                    }
                    queenMoveFields.clear();
                    for (int i = 1; i < 8; i++) {
                        for (int k = 1; k < 8; k++) {
                            if (i == k) {
                                if (row - i - 1 >= 0 & col - k - 1 >= 0)
                                    queenMoveFields.put(row - i - 1, col - k - 1);
                                if (canJump(player, row, col, row - i, col - k, row - i - 1, col - k - 1)) {
                                    moveList.add(new CheckersMove(row, col, row - i - 1, col - k - 1));
                                }
                            }
                        }
                    }
                    queenMoveFields.clear();
                    System.out.println("Skoki" + moveList);
                }
            }
        }
        if (moveList.size() == 0) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player) {
                        if (canMove(player, row, col, row + 1, col + 1))
                            moveList.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moveList.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moveList.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moveList.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                    if (board[row][col] == playerQueen) {
                        for (int i = 1; i < 8; i++) {
                            for (int k = 1; k < 8; k++) {
                                if (i == k) {
                                    if (row + i <= 7 & col + k <= 7)
                                        queenMoveFields.put(row + i, col + k);
                                    if (canMove(player, row, col, row + i, col + k)) {
                                        moveList.add(new CheckersMove(row, col, row + i, col + k));
                                    }
                                }
                            }
                        }
                        queenMoveFields.clear();
                        for (int i = 1; i < 8; i++) {
                            for (int k = 1; k < 8; k++) {
                                if (i == k) {
                                    if (row - i >= 0 & col + k <= 7)
                                        queenMoveFields.put(row - i, col + k);
                                    if (canMove(player, row, col, row - i, col + k)) {
                                        moveList.add(new CheckersMove(row, col, row - i, col + k));
                                    }
                                }
                            }
                        }
                        queenMoveFields.clear();
                        for (int i = 1; i < 8; i++) {
                            for (int k = 1; k < 8; k++) {
                                if (i == k) {
                                    if (row + i <= 7 & col - k >= 0)
                                        queenMoveFields.put(row + i, col - k);
                                    if (canMove(player, row, col, row + i, col - k)) {
                                        moveList.add(new CheckersMove(row, col, row + i, col - k));
                                    }
                                }
                            }
                        }
                        queenMoveFields.clear();
                        for (int i = 1; i < 8; i++) {
                            for (int k = 1; k < 8; k++) {
                                if (i == k) {
                                    if (row - i >= 0 & col - k >= 0)
                                        queenMoveFields.put(row - i, col - k);
                                    if (canMove(player, row, col, row - i, col - k)) {
                                        moveList.add(new CheckersMove(row, col, row - i, col - k));
                                    }
                                }
                            }
                        }
                        queenMoveFields.clear();
                        System.out.println("Ruchy" + moveList);
                    }
                }
            }
        }
        if (moveList.size() == 0) {
            return null;
        } else {
            return moveList;
        }
    }

    List<CheckersMove> getLegalJumpsFrom(int player, int row, int col) {
        if (player != RED && player != BLACK) {
            return null;
        }
        int playerQueen;
        if (player == RED)
            playerQueen = RED_QUEEN;
        else
            playerQueen = BLACK_QUEEN;

        ArrayList<CheckersMove> jumpList = new ArrayList<>();
        if (board[row][col] == player) {
            if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                jumpList.add(new CheckersMove(row, col, row + 2, col + 2));
            if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                jumpList.add(new CheckersMove(row, col, row - 2, col + 2));
            if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                jumpList.add(new CheckersMove(row, col, row + 2, col - 2));
            if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                jumpList.add(new CheckersMove(row, col, row - 2, col - 2));
        }
        if (board[row][col] == playerQueen) {
            for (int i = 1; i < 8; i++) {
                for (int k = 1; k < 8; k++) {
                    if (i == k) {
                        if (row + i + 1 <= 7 & col + k + 1 <= 7)
                            queenMoveFields.put(row + i + 1, col + k + 1);
                        if (canJump(player, row, col, row + i, col + k, row + i + 1, col + k + 1)) {
                            jumpList.add(new CheckersMove(row, col, row + i + 1, col + k + 1));
                        }
                    }
                }
            }
            queenMoveFields.clear();
            for (int i = 1; i < 8; i++) {
                for (int k = 1; k < 8; k++) {
                    if (i == k) {
                        if (row - i - 1 >= 0 & col + k + 1 <= 7)
                            queenMoveFields.put(row - i - 1, col + k + 1);
                        if (canJump(player, row, col, row - i, col + k, row - i - 1, col + k + 1)) {
                            jumpList.add(new CheckersMove(row, col, row - i - 1, col + k + 1));
                        }
                    }
                }
            }
            queenMoveFields.clear();
            for (int i = 1; i < 8; i++) {
                for (int k = 1; k < 8; k++) {
                    if (i == k) {
                        if (row + i + 1 <= 7 & col - k - 1 >= 0)
                            queenMoveFields.put(row + i + 1, col - k - 1);
                        if (canJump(player, row, col, row + i, col - k, row + i + 1, col - k - 1)) {
                            jumpList.add(new CheckersMove(row, col, row + i + 1, col - k - 1));
                        }
                    }
                }
            }
            queenMoveFields.clear();
            for (int i = 1; i < 8; i++) {
                for (int k = 1; k < 8; k++) {
                    if (i == k) {
                        if (row - i - 1 >= 0 & col - k - 1 >= 0)
                            queenMoveFields.put(row - i - 1, col - k - 1);
                        if (canJump(player, row, col, row - i, col - k, row - i - 1, col - k - 1)) {
                            jumpList.add(new CheckersMove(row, col, row - i - 1, col - k - 1));
                        }
                    }
                }
            }
            queenMoveFields.clear();
        }
        if (jumpList.size() == 0)
            return null;
        else {
            return jumpList;
        }
    }

}
