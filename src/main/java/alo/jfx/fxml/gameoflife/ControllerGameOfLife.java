package alo.jfx.fxml.gameoflife;

import alo.jfx.controller.MyClosable;
import alo.jfx.fxml.hub.ControllerHub;
import alo.jfx.model.Cell;
import alo.jfx.model.GameOfLife;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControllerGameOfLife implements MyClosable {

    private static final Logger logger = LogManager.getLogger(ControllerHub.class);

    private final Random random = new Random();
    private List<Number> possibleCellSize;
    private int xNbCells;
    private int yNbCells;
    private double nbCells;

    private GraphicsContext gc;
    private GameOfLife gameOfLife;
    private Timeline timeline;

    private int currentStep;
    private double cellSize;
    private Cell currentCell = new Cell(-1, -1);
    private int pickupState;

    private double defaultDuration;
    private int defaultMinCellSize;

    /**
     * List of possible cell sizes
     * Selecting one of them will create an new empty
     * {@link GameOfLife} board
     */
    @FXML
    private ComboBox<Number> comboBoxSize;

    /**
     * Slider to change the game of life update speed
     */
    @FXML
    private Slider sliderGameSpeed;

    /**
     * Label used to display the coordinates of the
     * cell under the mouse pointer
     */
    @FXML
    private Label labelCellCoordinates;

    /**
     * Label used to display the current cell size
     */
    @FXML
    private Label labelCellSize;

    /**
     * Canvas for displaying the {@link GameOfLife} board
     */
    @FXML
    private Canvas canvasGameOfLife;

    /**
     * Resume (do not create) the {@link GameOfLife}
     */
    @FXML
    public void onClickStartGameOfLife() {
        gameOfLifeResume();
    }

    /**
     * Stop the animation of the {@link GameOfLife}
     */
    @FXML
    public void onClickStopGameOfLife() {
        gameOfLifeStop();
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, update it one single time
     * and then display it
     */
    @FXML
    public void onClickSingleStepUpdate() {
        gameOfLifeStop();
        gameOfLifeUpdate();
        gameOfLifeDrawBoard();
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, create a new {@link GameOfLife}
     * without any living cell and then display it
     */
    @FXML
    public void onClickClear() {
        gameOfLifeStop();

        logger.info("Clear...");
        gameOfLife = new GameOfLife(xNbCells, yNbCells);
        currentStep = 0;

        gameOfLifeDrawBoard();
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, create a new {@link GameOfLife}
     * and populate it with randomly chosen living cells
     */
    @FXML
    public void onClickNewSeed() {
        gameOfLifeStop();

        logger.info("New Seed ...");
        gameOfLife = new GameOfLife(xNbCells, yNbCells);
        currentStep = 0;

        setupGridInitialCells();
        gameOfLifeDrawBoard();
    }

    /**
     * Method called by {@link javafx.fxml.FXMLLoader} after instantiating this controller
     * (Controller is referenced in the .fxml file)
     */
    public void initialize() {
        logger.debug("initialize: " + this.getClass().getCanonicalName());

        defaultMinCellSize = 12;
        defaultDuration = 0.5;

        setupCanvas();
        setupCellSize(defaultMinCellSize);
        setupBoardDimensions();
        setupGridInitialCells();
        setupGameSpeedSlider();
        setupSizeComboBox();
        setupTimeLine(defaultDuration);

        gameOfLifeDrawBoard();

    }

    private void eventChangeCellSize(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        logger.debug("comboBoxOnChangeHandleEvent old: " + oldValue + " new: " + newValue);
        gameOfLifeStop();

        cellSize = newValue.doubleValue();

        setupBoardDimensions();

        gameOfLife = new GameOfLife(xNbCells, yNbCells);

        gameOfLifeDrawBoard();
    }

    private void eventChangeGameSpeed(MouseEvent mouseEvent) {
//        logger.debug("SliderGameSpeed: Handle Mouse Event: " + mouseEvent.getEventType());

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            logger.debug("SliderGameSpeed New Value: " + sliderGameSpeed.getValue());
            Animation.Status previousStatus = timeline.getStatus();
            gameOfLifeStop();
            setupTimeLine(sliderGameSpeed.getValue());
            if (previousStatus == Animation.Status.RUNNING)
                timeline.play();
        }
    }


    private void eventActionOnCanvas(MouseEvent event) {
//        logger.debug("Canvas: Handle Mouse event: " + event.getEventType());

        Cell cell = getCellFromMouseCoordinates(event.getX(), event.getY());
        labelCellCoordinates.setText("Cell " + cell);

        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {

            if (!cell.equals(currentCell)) {
                currentCell = cell;
                logger.debug("Moved to another cell: " + currentCell);
            }

            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            logger.debug("Canvas Event: Mouse Dragged");

            if (!cell.equals(currentCell)) {
                currentCell = cell;
                logger.debug("Dragged to another cell: " + currentCell);

                gameOfLife.setState(cell.getX(), cell.getY(), pickupState);
                gameOfLifeDrawBoard();
            }

            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            logger.debug("Canvas Event: Mouse Pressed");

            pickupState = gameOfLife.alternateState(cell.getX(), cell.getY());
            gameOfLifeDrawBoard();

            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            logger.debug("Canvas Event: Mouse Released");

            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            logger.debug("Canvas Event: Mouse Clicked (" + event.getX() + ", " + event.getY() + ")");
        }
    }

    private Cell getCellFromMouseCoordinates(double x, double y) {
        return new Cell((int) (x / cellSize), (int) (y / cellSize));
    }

    private void gameOfLifeResume() {
        if (timeline.getStatus() != Animation.Status.RUNNING) {
            if (currentStep != 0) {
                logger.info("Resuming Game of life...");
            } else {
                logger.info("Starting Game of life...");
            }
            timeline.play();
        } else {
            logger.debug("Game of life is already running: Ignored...");
        }
    }

    private void gameOfLifeStop() {
        if (timeline.getStatus() != Animation.Status.RUNNING)
            return;

        logger.info("Stop Game of life!");
        timeline.stop();
    }

    private void gameOfLifeUpdate() {
        currentStep++;
        logger.debug("Step: " + currentStep);
        gameOfLife.updateBoard();
    }

    private void gameOfLifeDrawBoard() {
        drawAllCells();
        drawGrid();
    }

    private void setupSizeComboBox() {
        comboBoxSize.getItems().addAll(possibleCellSize);
        comboBoxSize.setValue(possibleCellSize.get(0));
        comboBoxSize.getSelectionModel().selectedItemProperty().addListener(this::eventChangeCellSize);
    }

    private void setupGameSpeedSlider() {
        sliderGameSpeed.setMin(0);
        sliderGameSpeed.setMax(1);
        sliderGameSpeed.setValue(defaultDuration);

        sliderGameSpeed.setShowTickMarks(true);
        sliderGameSpeed.setShowTickLabels(true);

        sliderGameSpeed.setMajorTickUnit(0.5);
        sliderGameSpeed.setMinorTickCount(4);

        sliderGameSpeed.setBlockIncrement(0.1);

        sliderGameSpeed.setSnapToTicks(true);

        sliderGameSpeed.addEventHandler(MouseEvent.ANY, this::eventChangeGameSpeed);
    }

    private void setupCanvas() {
        gc = canvasGameOfLife.getGraphicsContext2D();

        canvasGameOfLife.addEventHandler(MouseEvent.ANY, this::eventActionOnCanvas);
    }

    private void setupTimeLine(Double duration) {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(duration * 1000),
                        actionEvent -> {
                            gameOfLifeUpdate();
                            gameOfLifeDrawBoard();
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void setupBoardDimensions() {
        labelCellSize.setText("Cell Size: " + cellSize);
        logger.info("Cell size is: " + cellSize);

        xNbCells = (int) (canvasGameOfLife.getWidth() / cellSize);
        logger.info("Number of cells on X: " + xNbCells);

        yNbCells = (int) (canvasGameOfLife.getHeight() / cellSize);
        logger.info("Number of cells on Y: " + yNbCells);

        nbCells = xNbCells * yNbCells;
        logger.debug("Number of cells in the grid: " + nbCells);
    }

    private void setupGridInitialCells() {
        gameOfLife = new GameOfLife(xNbCells, yNbCells);

        double nbInitMaxCells = nbCells / 2 + random.nextInt(500);

        for (double nbCell = 0; nbCell < nbInitMaxCells; nbCell++) {
            gameOfLife.setAlive(random.nextInt(this.xNbCells), random.nextInt(yNbCells));
        }
    }

    private void setupCellSize(int minCellSize) {
        double width = canvasGameOfLife.getWidth();
        double height = canvasGameOfLife.getHeight();

        possibleCellSize = new ArrayList<>();

        int maxSize = (int) (width / 2);

        for (double size = minCellSize; size <= maxSize; size++) {
            if ((width % size) == 0) {
                if ((height % size) == 0)
                    possibleCellSize.add(size);
            }
        }

        cellSize = possibleCellSize.get(0).doubleValue();
    }

    private void drawCell(int x, int y, Paint color) {
        gc.setFill(color);

        // Parameters are x, y, offset x, offset y:
        gc.fillRect(x * cellSize, y * cellSize,
                cellSize, cellSize);
    }

    private void drawGrid() {
        gc.setStroke(Color.BLUE);

        for (int c = 0; c < xNbCells + 1; c++) {
            gc.strokeLine(c * cellSize, 0, c * cellSize, canvasGameOfLife.getHeight());
        }

        for (int l = 0; l < xNbCells + 1; l++) {
            gc.strokeLine(0, l * cellSize, canvasGameOfLife.getWidth(), l * cellSize);
        }
    }

    private void drawAllCells() {
        int[][] board = gameOfLife.getBoard();

        for (int x = 0; x < xNbCells; x++) {
            for (int y = 0; y < yNbCells; y++) {
                if (board[x][y] == 0)
                    drawCell(x, y, Color.BEIGE);
                else
                    drawCell(x, y, Color.BLACK);
            }
        }
    }

    @Override
    public void onCloseEvent() {
        logger.debug("Controller onCloseEvent: " + this.getClass().getCanonicalName());
        gameOfLifeStop();
    }
}
