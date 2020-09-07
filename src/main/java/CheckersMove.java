public class CheckersMove {

    int fromRow;
    int fromCol;
    int toRow;
    int toCol;

    public CheckersMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public boolean isJump() {
        if ((CheckersData.board[toRow][toCol] == CheckersData.RED || CheckersData.board[toRow][toCol] == CheckersData.BLACK) &
                (fromRow - toRow == 2 || fromRow - toRow == -2)) {
            return true;
        } else if ((CheckersData.board[toRow][toCol] == CheckersData.RED_QUEEN || CheckersData.board[toRow][toCol] == CheckersData.BLACK_QUEEN) &
                (fromRow - toRow >= 2 || fromRow - toRow <= -2)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "CheckersMove from: " + fromRow + "/" + fromCol + " to: " + toRow + "/" + toCol;
    }
}
