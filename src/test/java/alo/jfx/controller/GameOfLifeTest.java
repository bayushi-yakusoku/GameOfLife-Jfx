package alo.jfx.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOfLifeTest {

    @DisplayName("GameOfLifeTest: Test initCells with simple board 1x1 all dead")
    @Test
    public void initCellsAllDead() {
        GameOfLife gameOfLife = new GameOfLife(1, 1);
        gameOfLife.initCells(
                new Cell(1,1),
                new Cell(0,1),
                new Cell(-1,-1)
        );

        String expectedResult = "|-|\n";

        assertEquals(expectedResult, gameOfLife.getBoardString());
    }

    @DisplayName("GameOfLifeTest: Test initCells with simple board 1x1 all alive")
    @Test
    public void initCellsAllAlive() {
        GameOfLife gameOfLife = new GameOfLife(1, 1);
        gameOfLife.initCells(
                new Cell(0,0),
                new Cell(0,1),
                new Cell(-1,-1)
        );

        String expectedResult = "|*|\n";

        assertEquals(expectedResult, gameOfLife.getBoardString());
    }

    @DisplayName("GameOfLifeTest: Test setAlive for one cell")
    @Test
    public void setAliveOneCell() {
        GameOfLife gameOfLife = new GameOfLife(1, 1);

        // Preparation for the final test:
        int checkFirstStep = 0;
        assertEquals(checkFirstStep, gameOfLife.getState(0,0));

        // Final test:
        int expectedResult = 1;
        gameOfLife.setAlive(0,0);

        assertEquals(expectedResult, gameOfLife.getState(0,0));
    }

    @DisplayName("GameOfLifeTest: Test setDead for one cell")
    @Test
    public void setDeadOneCell() {
        GameOfLife gameOfLife = new GameOfLife(1, 1);

        // Preparation for the final test:
        gameOfLife.initCells(new Cell(0,0));
        int checkFirstStep = 1;
        assertEquals(checkFirstStep, gameOfLife.getState(0,0));

        // Final test:
        int expectedResult = 0;
        gameOfLife.setDead(0,0);

        assertEquals(expectedResult, gameOfLife.getState(0,0));
    }

    @DisplayName("GameOfLifeTest: Number of neighbors alive should be 2")
    @Test
    public void getNbNeighborsAliveEqual2() {
        GameOfLife gameOfLife = new GameOfLife(10, 10);
        gameOfLife.initCells(
                new Cell(0, 0),
                new Cell(0, 1),
                new Cell(1, 0));

        int expectedResult = 2;

        assertEquals(expectedResult, gameOfLife.getNbNeighborsAlive(0, 0),
                "Number of living neighbors is wrong!");
    }

    @DisplayName("GameOfLifeTest: Number of neighbors alive should be 5")
    @Test
    public void getNbNeighborsAliveEqual5() {
        GameOfLife gameOfLife = new GameOfLife(10, 10);
        gameOfLife.initCells(
                new Cell(0, 0),
                new Cell(0, 1),
                new Cell(1, 0),
                new Cell(2, 0),
                new Cell(2, 2));

        int expectedResult = 5;

        assertEquals(expectedResult, gameOfLife.getNbNeighborsAlive(1, 1),
                "Number of living neighbors is wrong!");
    }

}
