import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckersBoard extends Canvas {

    Pane root = new Pane();
    Scene scene = new Scene(root, 1200, 900);
    Canvas canvas;

    CheckersData checkersData = new CheckersData();
    boolean gameInProgress;
    int currentPlayer;
    int selectedRow;
    int selectedCol;
    int playersPoints = 0;
    int computersPoints = 0;
    List<CheckersMove> legalMoves;
    Label message;
    Button newGameButton;
    Button restartGameButton;
    Button cancelGameButton;
    Label computerScores;
    Label playerScores;
    Label scores;
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public CheckersBoard() {

        computerScores = new Label();
        playerScores = new Label();
        scores = new Label();
        root.getChildren().addAll(scores, computerScores, playerScores);
        prepareBackground();
        createButtons();
        createBoard();
        message = new Label("Click \"New Game\" to start.");
        message.setFont(new Font(20));
        message.relocate(900, 480);
        root.getChildren().add(message);
        scene.setOnMousePressed(this::mousePressed);
    }

    public void prepareBackground() {

        Image windowImage = new Image("file:src/main/resources/checkersBackground.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(windowImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        root.setBackground(background);

    }

    public void createBoard() {
        //Creating Checkers Board

        canvas = new Canvas(806, 806);
        canvas.setLayoutX(78);
        canvas.setLayoutY(20);
        GraphicsContext board = canvas.getGraphicsContext2D();
        board.setStroke(Color.BLACK);
        board.setLineWidth(3);
        board.strokeRect(1, 1, 804, 804);

        //Creating board fields
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2) {
                    board.setFill(Color.LIGHTGRAY);
                } else {
                    board.setFill(Color.DARKGREY);
                }
                board.fillRect(3 + col * 100, 3 + row * 100, 100, 100);
            }
        }

        //Adding A-H & 1-8 coordinates around the board
        for (int i = 0; i < 8; i++) {
            Text rowNumber = new Text(35, 780 - (i) * 100, String.valueOf((i) + 1));
            rowNumber.setFont(new Font(Font.getFontNames().get(0), 25));
            root.getChildren().remove(rowNumber);
            root.getChildren().add(rowNumber);
        }
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int k = 0; k < 8; k++) {
            Text colLetter = new Text(120 + k * 100, 875, letters[k]);
            colLetter.setFont(new Font(Font.getFontNames().get(0), 25));
            root.getChildren().remove(colLetter);
            root.getChildren().add(colLetter);
        }

        Image redPawnImage = new Image("file:src/main/resources/RedPawn.png");
        Image blackPawnImage = new Image("file:src/main/resources/BlackPawn.png");
        Image redPawnQueenImage = new Image("file:src/main/resources/RedPawnQueen.png");
        Image blackPawnQueenImage = new Image("file:src/main/resources/BlackPawnQueen.png");

        //Filling board with pawns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (checkersData.pieceAt(row, col)) {
                    case (CheckersData.RED):
                        ImageView redPon = new ImageView();
                        board.drawImage(redPawnImage, 17 + col * 100, 17 + row * 100, 70, 70);
                        root.getChildren().add(redPon);
                        break;
                    case (CheckersData.BLACK):
                        ImageView blackPon = new ImageView();
                        board.drawImage(blackPawnImage, 17 + col * 100, 17 + row * 100, 70, 70);
                        root.getChildren().add(blackPon);
                        break;
                    case (CheckersData.RED_QUEEN):
                        ImageView redPonQueen = new ImageView();
                        board.drawImage(redPawnQueenImage, 15 + col * 100, 15 + row * 100, 70, 70);
                        root.getChildren().add(redPonQueen);
                        break;
                    case (CheckersData.BLACK_QUEEN):
                        ImageView blackPonQueen = new ImageView();
                        board.drawImage(blackPawnQueenImage, 15 + col * 100, 15 + row * 100, 70, 70);
                        root.getChildren().add(blackPonQueen);
                        break;
                }
            }
        }

        root.getChildren().add(canvas);
    }

    public void refreshBoard() {

        GraphicsContext board = canvas.getGraphicsContext2D();

        //Refreshing board fields
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2) {
                    board.setFill(Color.LIGHTGRAY);
                } else {
                    board.setFill(Color.DARKGREY);
                }
                board.fillRect(3 + col * 100, 3 + row * 100, 100, 100);
            }
        }

        Image redPawnImage = new Image("file:src/main/resources/RedPawn.png");
        Image blackPawnImage = new Image("file:src/main/resources/BlackPawn.png");
        Image redPawnQueenImage = new Image("file:src/main/resources/RedPawnQueen.png");
        Image blackPawnQueenImage = new Image("file:src/main/resources/BlackPawnQueen.png");

        //Refreshing pawns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (checkersData.pieceAt(row, col)) {
                    case (CheckersData.RED):
                        ImageView redPon = new ImageView();
                        board.drawImage(redPawnImage, 17 + col * 100, 17 + row * 100, 70, 70);
                        root.getChildren().add(redPon);
                        break;
                    case (CheckersData.BLACK):
                        ImageView blackPon = new ImageView();
                        board.drawImage(blackPawnImage, 17 + col * 100, 17 + row * 100, 70, 70);
                        root.getChildren().add(blackPon);
                        break;
                    case (CheckersData.RED_QUEEN):
                        ImageView redPonQueen = new ImageView();
                        board.drawImage(redPawnQueenImage, 15 + col * 100, 15 + row * 100, 70, 70);
                        root.getChildren().add(redPonQueen);
                        break;
                    case (CheckersData.BLACK_QUEEN):
                        ImageView blackPonQueen = new ImageView();
                        board.drawImage(blackPawnQueenImage, 15 + col * 100, 15 + row * 100, 70, 70);
                        root.getChildren().add(blackPonQueen);
                        break;
                }
            }
        }

    }

    public void createButtons() {

        newGameButton = new Button("New Game");
        restartGameButton = new Button("Restart Game");
        cancelGameButton = new Button("Cancel Game");

        newGameButton.setManaged(false);
        newGameButton.resizeRelocate(900, 130, 250, 70);
        restartGameButton.setManaged(false);
        restartGameButton.resizeRelocate(900, 230, 250, 70);
        cancelGameButton.setManaged(false);
        cancelGameButton.resizeRelocate(900, 330, 250, 70);

        newGameButton.setOnAction(click -> startNewGame());
        restartGameButton.setOnAction(click -> restartGame());
        cancelGameButton.setOnAction(click -> closeGame());

        root.getChildren().addAll(newGameButton, restartGameButton, cancelGameButton);

    }

    public void closeGame() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.getButtonTypes().removeAll(ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Exit game");
        alert.setContentText("Do You want to leave the game ?");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent()) {
            if (res.get().equals(ButtonType.YES))
                Platform.exit();
        }
    }

    public void createLabels() {

        scores.setText("Scores:");
        scores.setFont(new Font(20));
        scores.relocate(900, 680);

        playerScores.setText("Player: " + playersPoints);
        playerScores.setFont(new Font(20));
        playerScores.relocate(900, 730);

        computerScores.setText("Computer: " + computersPoints);
        computerScores.setFont(new Font(20));
        computerScores.relocate(900, 760);

    }

    public void startNewGame() {

        newGameButton.setDisable(true);
        checkersData.prepareGameData();
        currentPlayer = CheckersData.BLACK;
        message.setText("Player - BLACK\nComputer - RED\n\nPlayer starts, make Your move.");
        createLabels();
        legalMoves = checkersData.getLegalMoves(currentPlayer);
        selectedRow = -1;
        selectedCol = -1;
        gameInProgress = true;
        refreshBoard();

    }

    public void restartGame() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.getButtonTypes().removeAll(ButtonType.CANCEL, ButtonType.OK);
        alert.setTitle("Restart game");
        alert.setContentText("Are Your sure to start over?\nIf You do, Computer is going to win this game.");
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent()) {
            if (res.get().equals(ButtonType.YES))
                computersPoints++;
            startNewGame();
        }
    }

    public void gameOver(String text) {
        message.setText(text);
        newGameButton.setDisable(false);
        restartGameButton.setDisable(true);
        gameInProgress = false;
    }

    public void doMakeMove(CheckersMove move) {

        checkersData.makeMove(move);
        refreshBoard();

        if (move.isJump()) {
            if (currentPlayer == CheckersData.BLACK) {
                legalMoves = checkersData.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    selectedRow = move.toRow;
                    selectedCol = move.toCol;
                    refreshBoard();
                    GraphicsContext board = canvas.getGraphicsContext2D();
                    board.setStroke(Color.GREEN);
                    board.setLineWidth(4);
                    board.strokeRect(5 + selectedCol * 100, 5 + selectedRow * 100, 96, 96);
                    return;
                }
            } else {
                legalMoves = checkersData.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    selectedRow = move.toRow;
                    selectedCol = move.toCol;
                    refreshBoard();
                    GraphicsContext board = canvas.getGraphicsContext2D();
                    board.setStroke(Color.RED);
                    board.setLineWidth(4);
                    board.strokeRect(5 + selectedCol * 100, 5 + selectedRow * 100, 96, 96);
                        computerMove();
                    return;
                }
            }
        }

        if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            message.setText("Your turn.");
            legalMoves = checkersData.getLegalMoves(currentPlayer);
            selectedRow = -1;
            selectedCol = -1;
            if (legalMoves == null) {
                gameOver("BLACK has no moves.\nRED wins.\n\nClick \"New Game\" to play again.");
                computersPoints++;
            }
        } else {
            currentPlayer = CheckersData.RED;
            message.setText("Computer's turn.");
            legalMoves = checkersData.getLegalMoves(currentPlayer);
            selectedRow = -1;
            selectedCol = -1;
            if (legalMoves == null) {
                gameOver("RED has no moves.\nBLACK wins.\n\nClick \"New Game\" to play again.");
                playersPoints++;
            } else {
                    computerSelectPawn();
                //executorService.schedule(this::computerMove, 1, TimeUnit.SECONDS);
                //executorService.shutdown();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Error");
                }
                    computerMove();
            }
        }
    }

    public void computerSelectPawn() {

        GraphicsContext board = canvas.getGraphicsContext2D();
        Random random = new Random();

        if (selectedRow == -1 & selectedCol == -1) {
            for (int k = 0; k < 999999; k++) {
                int randomFromRow = random.nextInt(8);
                int randomFromCol = random.nextInt(8);
                for (int i = 0; i < legalMoves.size(); i++) {
                    if (randomFromRow % 2 != randomFromCol % 2 & (checkersData.pieceAt(randomFromRow, randomFromCol) == CheckersData.RED ||
                            checkersData.pieceAt(randomFromRow, randomFromCol) == CheckersData.RED_QUEEN) &
                            legalMoves.get(i).fromRow == randomFromRow & legalMoves.get(i).fromCol == randomFromCol) {
                        selectedRow = randomFromRow;
                        selectedCol = randomFromCol;
                        board.setStroke(Color.RED);
                        board.setLineWidth(4);
                        board.strokeRect(5 + randomFromCol * 100, 5 + randomFromRow * 100, 96, 96);
                        return;
                    }
                }
            }
        }
    }

    public void computerMove() {

        Random random = new Random();

        for (int k = 0; k < 999999; k++) {
            int randomToRow = random.nextInt(8);
            int randomToCol = random.nextInt(8);
            for (int i = 0; i < legalMoves.size(); i++) {
                if (randomToRow % 2 != randomToCol % 2 & checkersData.pieceAt(randomToRow, randomToCol) == CheckersData.EMPTY &
                        legalMoves.get(i).fromRow == selectedRow & legalMoves.get(i).fromCol == selectedCol &
                        legalMoves.get(i).toRow == randomToRow & legalMoves.get(i).toCol == randomToCol) {
                    try {
                        doMakeMove(legalMoves.get(i));
                    } catch (Exception e) {
                        e.fillInStackTrace();
                    }
                    return;
                }
            }
        }

    }

    public void clickSquare(int row, int col) {
        GraphicsContext board = canvas.getGraphicsContext2D();

        for (int i = 0; i < legalMoves.size(); i++) {
            if (legalMoves.get(i).fromRow == row && legalMoves.get(i).fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                board.setStroke(Color.GREEN);
                board.setLineWidth(4);
                board.strokeRect(5 + col * 100, 5 + row * 100, 96, 96);
                return;
            }
        }

        for (int i = 0; i < legalMoves.size(); i++) {
            if (legalMoves.get(i).fromRow == selectedRow & legalMoves.get(i).fromCol == selectedCol
                    & legalMoves.get(i).toRow == row & legalMoves.get(i).toCol == col) {
                doMakeMove(legalMoves.get(i));
                return;
            }
        }
    }

    public void mousePressed(MouseEvent evt) {
        refreshBoard();
        if (gameInProgress & currentPlayer == CheckersData.BLACK) {
            int col = (int) (((evt.getX() - 81) / 100));
            int row = (int) ((evt.getY() - 23) / 100);
            if (col >= 0 & col < 8 & row >= 0 & row < 8) {
                clickSquare(row, col);
            }
        }
    }
}
