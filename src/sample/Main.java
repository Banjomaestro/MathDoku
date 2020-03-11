package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private VBox mainVBox;
    private GridPane myGridPane;
    private FlowPane myFlowPane;
    private MathDokuGrid myMathDokuGrid;
    private ArrayList<Cell> myCells;
    private ArrayList<Cage> myCages;
    private boolean gameCompleted = false;
    private ArrayList<Action> pastActions = new ArrayList<Action>();
    private ArrayList<Action> futureAction = new ArrayList<Action>();
    private boolean userChange = true;
    private boolean showMistakes = false;
    private Button showMistakesButton;
    private boolean threadRun = false;
    private Task task;
    private Thread theThread;
    private Button redo;
    private Button undo;
    private Button changeFont;

    @Override
    public void start(Stage primaryStage) {

        mainVBox = new VBox();
        myGridPane = new GridPane();
        myFlowPane = new FlowPane();

        myGridPane.setAlignment(Pos.CENTER);
        myFlowPane.setHgap(10);
        myFlowPane.setVgap(10);

        myFlowPane.setAlignment(Pos.CENTER);

        mainVBox.setAlignment(Pos.BOTTOM_CENTER);

        Button loadFromFile = new Button("Load Game from file");
        Button loadFromText = new Button("Load Game from text input");
        showMistakesButton = new Button("show mistakes");
        Button generateGame = new Button("Generate random Grid");
        undo = new Button("undo");
        redo = new Button("redo");
        Button clearGrid = new Button("clear the grid");
        Button hint = new Button("hint");

        undo.setDisable(true);
        redo.setDisable(true);

        loadFromFile.setOnAction((e) -> {
            try {
                loadGameFromFile(primaryStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        loadFromText.setOnAction((e) -> loadGameFromText());
        showMistakesButton.setOnAction((e) -> showMistakes());
        generateGame.setOnAction((e) -> generateGrid());
        undo.setOnAction((e) -> undo());
        redo.setOnAction((e) -> redo());
        clearGrid.setOnAction((e) -> clearGrid());
        hint.setOnAction((e) -> hint());

        myFlowPane.getChildren().addAll(loadFromFile, loadFromText, showMistakesButton, generateGame, undo, redo, clearGrid, hint);
        mainVBox.getChildren().addAll(myGridPane, myFlowPane);
        mainVBox.setMargin(myGridPane, new Insets(10));

        VBox.setVgrow(myGridPane, Priority.ALWAYS);
        FlowPane.setMargin(loadFromFile, new Insets(10));
        FlowPane.setMargin(hint, new Insets(10));

        primaryStage.setTitle("MathDoku");
        primaryStage.setScene(new Scene(mainVBox, 900, 800));
        primaryStage.show();
        myMathDokuGrid = new MathDokuGrid();
        myMathDokuGrid.generate(4);
        drawGrid();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void hint() {

        boolean isFalse = false;
        final boolean[] first = {true};
        final String[] oldValue = new String[1];
        final boolean[] running = {true};
        int cellValue = 0;
        Cell cellToCheck = null;

        while (!isFalse) {

            cellToCheck = myCells.get(new Random().nextInt(myCells.size() - 1));

            try {
                cellValue = Integer.parseInt(cellToCheck.getTheTextField().getText());
            } catch (NumberFormatException e) {
                cellValue = 0;

            }

            if (cellToCheck.getCorrectValue() != cellValue) {
                isFalse = true;


            }

        }
        Timer theTimer = new java.util.Timer();
        Cell finalCellToCheck = cellToCheck;
        oldValue[0] = finalCellToCheck.getTheTextField().getText();
        userChange = false;
        finalCellToCheck.getTheTextField().setText(finalCellToCheck.getCorrectValue() + "");
        userChange = true;
        theTimer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        userChange = false;
                        finalCellToCheck.getTheTextField().setText(oldValue[0]);
                        userChange = true;
                        running[0] = false;
                        theTimer.purge();

                    }

                },
                500
        );

    }

    public void undo() {

        if (pastActions.size() > 0) {
            userChange = false;
            futureAction.add(new Action(pastActions.get(pastActions.size() - 1).getTheTextField(), pastActions.get(pastActions.size() - 1).getTheTextField().getText()));
            pastActions.get(pastActions.size() - 1).getTheTextField().setText(pastActions.get(pastActions.size() - 1).getValue());
            pastActions.remove(pastActions.size() - 1);

            userChange = true;
        }
        if (pastActions.isEmpty()) {
            undo.setDisable(true);
        }

    }

    public void redo() {

        if (futureAction.size() > 0) {
            userChange = false;
            pastActions.add(new Action(futureAction.get(futureAction.size() - 1).getTheTextField(), futureAction.get(futureAction.size() - 1).getTheTextField().getText()));
            futureAction.get(futureAction.size() - 1).getTheTextField().setText(futureAction.get(futureAction.size() - 1).getValue());
            futureAction.remove(futureAction.size() - 1);
            userChange = true;
        }
        if (futureAction.isEmpty()) {
            redo.setDisable(true);
        }

    }

    public void clearGrid() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to clear the grid ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            for (Cell currentCell : myCells) {
                currentCell.getTheTextField().setText("");
                pastActions.removeAll(pastActions);
                futureAction.removeAll(futureAction);
            }
        }

    }

    public void generateGrid() {

        AtomicBoolean isInt = new AtomicBoolean(false);

        while (!isInt.get()) {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Generate a random Grid");
            dialog.setTitle("What should by the number of column and rows?");
            dialog.setContentText("Number of row and columns : ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(theResult -> {

                try {
                    Integer n = Integer.valueOf(theResult);
                    myMathDokuGrid = new MathDokuGrid();
                    myMathDokuGrid.generate(n);
                    isInt.set(true);
                } catch (NumberFormatException ex) {

                }

            });

        }
        drawGrid();
    }

    public void loadGameFromFile(Stage theStage) throws IOException {

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File selectedFile = fileChooser.showOpenDialog(theStage);

        Reader myReader = new Reader();
        myMathDokuGrid = myReader.readConfig(selectedFile);
        drawGrid();
        solveGrid();


    }

    public void loadGameFromText() {

        Reader myReader = new Reader();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enter your configuration", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Enter the configuration");
        alert.setHeaderText("Enter the desired configuration");
        alert.setContentText("Enter the desired configuration");

        TextArea textArea = new TextArea();

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            myMathDokuGrid = myReader.analyseString(textArea.getText());
            drawGrid();
            solveGrid();
        }
    }

    public void showMistakes() {

        showMistakes = !showMistakes;


        if (showMistakes) {
            showMistakesButton.setText("Hide mistakes");
            checkMistakes();
        } else {
            showMistakesButton.setText("Show mistakes");
        }

    }

    public void drawGrid() {

        threadRun = false;
        theThread = null;
        if (task != null) {
            task.cancel();
        }
        pastActions = new ArrayList<Action>();
        futureAction = new ArrayList<Action>();

        myGridPane.getChildren().clear();
        myCells = myMathDokuGrid.getMyCells();
        myCages = myMathDokuGrid.getMyCages();
        myMathDokuGrid.setBorders();

        for (Cell currentCell : myCells) {

            VBox cellVbox = currentCell.getTheVBox();
            cellVbox.setOnMouseClicked((e) -> onCellClick(currentCell));
            currentCell.getTheTextField().textProperty().addListener((observable, oldValue, newValue) -> {

                checkMistakes();

                if (userChange) {
                    pastActions.add(new Action(currentCell.getTheTextField(), oldValue));
                    futureAction.clear();
                }
                if (pastActions.isEmpty()) {
                    undo.setDisable(true);
                } else {
                    undo.setDisable(false);
                }
                if (futureAction.isEmpty()) {
                    redo.setDisable(true);
                } else {
                    redo.setDisable(false);
                }

            });
            myGridPane.add(cellVbox, currentCell.getPosition() % myMathDokuGrid.getN(), (int) currentCell.getPosition() / myMathDokuGrid.getN(), 1, 1);
        }

    }

    public void onCellClick(Cell theCell) {

        if (theCell.getTheTextField().getText().equals("")) {
            theCell.getTheTextField().setText("1");
        } else if (Integer.parseInt(theCell.getTheTextField().getText()) >= myMathDokuGrid.getN()) {
            theCell.getTheTextField().setText("1");
        } else {
            theCell.getTheTextField().setText((Integer.parseInt(theCell.getTheTextField().getText()) + 1) + "");
        }

    }

    public void checkMistakes() {

        gameCompleted = true;

        for (Cell currentCell : myCells) {
            currentCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }


        for (Cell currentCell : myCells) {

            for (Cell otherCell : myCells) {
                if (otherCell.getTheTextField().getText().equals(currentCell.getTheTextField().getText()) && !currentCell.getTheTextField().getText().equals("")) {

                    if (otherCell.getPosition() % myMathDokuGrid.getN() == currentCell.getPosition() % myMathDokuGrid.getN() && currentCell.getPosition() != otherCell.getPosition()) {

                        gameCompleted = false;
                        if (showMistakes) {
                            otherCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                            currentCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        }

                    }

                    if (otherCell.getPosition() / myMathDokuGrid.getN() == currentCell.getPosition() / myMathDokuGrid.getN() && currentCell.getPosition() != otherCell.getPosition()) {
                        gameCompleted = false;
                        if (showMistakes) {
                            otherCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                            currentCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                        }


                    }
                }
            }
        }

        for (Cage currentCage : myCages) {

            Integer result = null;
            int iterations = 0;
            boolean isCorrect = true;
            boolean allFilled = true;

            for (Cell currentCell : currentCage.getMyCells()) {

                iterations++;

                if (currentCell.getTheTextField().getText().equals("")) {
                    allFilled = false;
                    gameCompleted = false;
                    break;
                } else if (iterations == 1) {
                    result = Integer.parseInt(currentCell.getTheTextField().getText());
                } else {

                    if (currentCage.getOperator() == 'x') {
                        result *= Integer.parseInt(currentCell.getTheTextField().getText());
                    } else if (currentCage.getOperator() == '+') {
                        result += Integer.parseInt(currentCell.getTheTextField().getText());
                    } else if (currentCage.getOperator() == '-') {
                        result -= Integer.parseInt(currentCell.getTheTextField().getText());
                    } else if (currentCage.getOperator() == '÷') {
                        result /= Integer.parseInt(currentCell.getTheTextField().getText());
                    }
                }


            }

            if (allFilled) {
                if (currentCage.getGoal() != result) {
                    isCorrect = false;
                    gameCompleted = false;
                }

                if (!isCorrect) {
                    for (Cell currentCell : currentCage.getMyCells()) {
                        if (showMistakes)
                            currentCell.getTheVBox().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    }

                }
            }
        }

        if (gameCompleted) {
            threadRun = true;
            gameCompleted();

        }
    }

    public void gameCompleted() {


        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            public void run() {
                for (Cell currentCell : myCells) {
                    currentCell.getTheVBox().setBackground(new Background(new BackgroundFill(generateColor(),
                            CornerRadii.EMPTY,
                            Insets.EMPTY)));
                }

            }
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 500, TimeUnit.MILLISECONDS);


        if (theThread == null) {
            //theThread = new Thread(task);
            //theThread.start();
            //task.run();

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You won ! Do you want to play another Game?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {


            executor.shutdown();
            generateGrid();


        } else {
            System.exit(0);
        }


    }

    public Color generateColor() {
        Color color = Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        return color;
    }

    public void solveGrid() {

        boolean solution = false;
        boolean invalid = false;
        int numberOfFailures = 0;

        while(!solution){

            numberOfFailures++;
            if(numberOfFailures>500000){
                System.out.println("failure");
                break;
            }

            invalid = false;

            for(Cell currentCell :myCells){
                currentCell.setCorrectValue(new Random().nextInt(myMathDokuGrid.getN())+1);
            }

        for (Cell currentCell : myCells) {

            for (Cell otherCell : myCells) {
                if (otherCell.getCorrectValue()==currentCell.getCorrectValue()) {

                    if (otherCell.getPosition() % myMathDokuGrid.getN() == currentCell.getPosition() % myMathDokuGrid.getN() && currentCell.getPosition() != otherCell.getPosition()) {

                        invalid = true;

                    }

                    if (otherCell.getPosition() / myMathDokuGrid.getN() == currentCell.getPosition() / myMathDokuGrid.getN() && currentCell.getPosition() != otherCell.getPosition()) {
                        invalid = true;

                    }
                }
                if(invalid)
                    break;
            }
            if (invalid)
                break;
        }
        if (invalid)
            continue;

        for (Cage currentCage : myCages) {

            Integer result = null;
            int iterations = 0;
            boolean isCorrect = true;
            boolean allFilled = true;

            for (Cell currentCell : currentCage.getMyCells()) {

                iterations++;

                if (iterations == 1) {
                    result = currentCell.getCorrectValue();
                } else {

                    if (currentCage.getOperator() == 'x') {
                        result *= currentCell.getCorrectValue();
                    } else if (currentCage.getOperator() == '+') {
                        result += currentCell.getCorrectValue();
                    } else if (currentCage.getOperator() == '-') {
                        result -= currentCell.getCorrectValue();
                    } else if (currentCage.getOperator() == '÷') {
                        result /= currentCell.getCorrectValue();
                    }
                }


            }

                if (currentCage.getGoal() != result) {
                    invalid =true;
                    break;
                }

        }
        if (invalid)
            continue;
        else
            break;
        }
    }
}
