import java.util.HashMap;
import java.util.Map;

public class CheckersMove {

    String name;
    int fromRow;
    int fromCol;
    int toRow;
    int toCol;
    Map<Integer, Integer> fieldToErase;

    public CheckersMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.fieldToErase = new HashMap<>();
    }

    public void setFieldToErase(int row, int col) {
        fieldToErase.put(row, col);
    }

    public Map<Integer, Integer> getFieldToErase() {
        return fieldToErase;
    }

    public boolean isJump() {
        if ((CheckersData.board[toRow][toCol] == CheckersData.RED || CheckersData.board[toRow][toCol] == CheckersData.BLACK) &
                (fromRow - toRow == 2 || fromRow - toRow == -2)) {
            return true;
        }else if ((CheckersData.board[toRow][toCol] == CheckersData.RED_QUEEN || CheckersData.board[toRow][toCol] == CheckersData.BLACK_QUEEN) &
        this.fieldToErase.size() > 0) {
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
