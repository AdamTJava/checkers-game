import javafx.application.Application;
import javafx.stage.Stage;

public class CheckersMain extends Application {
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

            CheckersBoard checkersBoard = new CheckersBoard();

            primaryStage.setTitle("Checkers");
            primaryStage.setScene(checkersBoard.scene);
            primaryStage.show();

        }
}
