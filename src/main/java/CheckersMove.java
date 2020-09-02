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
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }

    @Override
    public String toString() {
        return "CheckersMove from: " + fromRow + "/" + fromCol + " to: " + toRow + "/" + toCol;
    }
}
