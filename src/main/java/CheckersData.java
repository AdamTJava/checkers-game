import java.util.ArrayList;
import java.util.List;

public class CheckersData {

    static final int
            EMPTY = 0,
            RED = 1,
            RED_QUEEN = 2,
            BLACK = 3,
            BLACK_QUEEN = 4;

    int[][] board;

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
            if (board[rowFrom][colFrom] == RED & rowTo < rowFrom) {
                return false;
            } else if (board[rowMiddle][colMiddle] != BLACK & board[rowMiddle][colMiddle] != BLACK_QUEEN) {
                return false;
            } else {
                return true;
            }
        } else {
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

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player || board[row][col] == playerQueen) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moveList.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moveList.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moveList.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moveList.add(new CheckersMove(row, col, row - 2, col - 2));
                }
            }
        }
        if (moveList.size() == 0) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerQueen) {
                        if (canMove(player, row, col, row + 1, col + 1))
                            moveList.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moveList.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moveList.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moveList.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                    //próba zrobienia osobnej logiki dla królowej, która morze chodzić po czym chce, co jest czarnym polem
                    //dosyć prymitywna i nie udało mi się jej rozwinąć, ale zostawiam poglądowo.
                    //if (board[row][col] == playerQueen) {
                    //    for (int i = 0; i < 8; i++) {
                    //        for (int k = 0; k < 8; k++) {
                    //           if (canMove(player, row, col, i, k) & i % 2 != k % 2) {
                    //                moveList.add(new CheckersMove(row, col, i, k));
                    //            }
                    //        }
                    //    }
                    //}
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
        if (board[row][col] == player || board[row][col] == playerQueen) {
            if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                jumpList.add(new CheckersMove(row, col, row + 2, col + 2));
            if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                jumpList.add(new CheckersMove(row, col, row - 2, col + 2));
            if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                jumpList.add(new CheckersMove(row, col, row + 2, col - 2));
            if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                jumpList.add(new CheckersMove(row, col, row - 2, col - 2));
        }
        if (jumpList.size() == 0)
            return null;
        else {
            return jumpList;
        }
    }

}
