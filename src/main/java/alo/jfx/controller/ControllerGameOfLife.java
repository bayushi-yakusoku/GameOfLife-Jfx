package alo.jfx.controller;

import alo.jfx.model.GameOfLife;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class ControllerGameOfLife implements MyClosable {

    private static final Logger logger = LogManager.getLogger(PrimaryController.class);

    private int xNbCells;
    private int yNbCells;
    private double nbCells;

    private GraphicsContext gc;
    private GameOfLife gameOfLife;
    private Timeline timeline;

    private int currentStep;

    private double cellSize;
    private double nbInitMaxCells;

    private int minCellSize;

    private Random random;

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
        resumeGameOfLife();
    }

    /**
     * Stop the animation of the {@link GameOfLife}
     */
    @FXML
    public void onClickStopGameOfLife() {
        stopGameOfLife();
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, update it one single time
     * and then display it
     */
    @FXML
    public void onClickSingleStepUpdate() {
        stopGameOfLife();
        updateGameOfLife();
        drawGameOfLife();
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, create a new {@link GameOfLife}
     * without any living cell and then display it
     */
    @FXML
    public void onClickClear() {
        stopGameOfLife();
        logger.info("Clear...");
        gameOfLife = new GameOfLife(xNbCells, yNbCells);
        drawGameOfLife();
        currentStep = 0;
    }

    /**
     * Stop the animation of {@link GameOfLife} if needed, create a new {@link GameOfLife}
     * and populate it with randomly chosen living cells
     */
    @FXML
    public void onClickNewSeed() {
        stopGameOfLife();

        logger.info("New Seed ...");
        gameOfLife = new GameOfLife(xNbCells, yNbCells);
        randomlyInitGridCells();
        drawGameOfLife();
        currentStep = 0;
    }

    /**
     * Method called by {@link javafx.fxml.FXMLLoader} after instantiating this controller
     * (Controller is referenced in the .fxml file)
     */
    public void initialize() {
        logger.debug("initialize: " + this.getClass().getCanonicalName());

        random = new Random();
        gc = canvasGameOfLife.getGraphicsContext2D();

        minCellSize = 12;

        updateConfiguration();

        randomlyInitGridCells();

        drawGameOfLife();

        initTimeLine();
    }

    private void initTimeLine() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(500),
                        actionEvent -> {
                            updateGameOfLife();
                            drawGameOfLife();
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void updateConfiguration() {
        cellSize = calcCellSize();
        logger.info("Cell size is: " + cellSize);

        xNbCells = (int) (canvasGameOfLife.getWidth() / getCellSize());
        logger.info("Number of cells on X: " + xNbCells);

        yNbCells = (int) (canvasGameOfLife.getHeight() / getCellSize());
        logger.info("Number of cells on Y: " + yNbCells);

        nbCells = xNbCells * yNbCells;
        logger.debug("Number of cells in the grid: " + nbCells);
    }

    private void resumeGameOfLife() {
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

    private void stopGameOfLife() {
        if (timeline.getStatus() != Animation.Status.RUNNING)
            return;

        logger.info("Stop Game of life!");
        timeline.stop();
    }

    private void updateGameOfLife() {
        currentStep++;
        logger.debug("Step: " + currentStep);
        gameOfLife.updateBoard();
    }

    private void drawGameOfLife() {
        drawAllCells();
        drawGrid();
    }

    private void randomlyInitGridCells() {
        gameOfLife = new GameOfLife(xNbCells, yNbCells);

        nbInitMaxCells = nbCells / 2 + random.nextInt(500);

        for (double nbCell = 0; nbCell < nbInitMaxCells; nbCell++) {
            gameOfLife.setAlive(random.nextInt(this.xNbCells), random.nextInt(yNbCells));
        }
    }

    private double calcCellSize() {
        double width = canvasGameOfLife.getWidth();
        double height = canvasGameOfLife.getHeight();

        int maxSize = (int) (width / 2);

        for (double size = minCellSize; size <= maxSize; size++) {
            if ((width % size) == 0) {
                if ((height % size) == 0)
                    return size;
            }
        }

        return minCellSize;
    }

    public double getCellSize() {
        return cellSize;
    }

    private void fillGridCell(int x, int y, Paint color) {
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
                    fillGridCell(x, y, Color.BEIGE);
                else
                    fillGridCell(x, y, Color.BLACK);
            }
        }
    }

    @Override
    public void onCloseEvent() {
        logger.debug("Controller onCloseEvent: " + this.getClass().getCanonicalName());
        stopGameOfLife();
    }
}
