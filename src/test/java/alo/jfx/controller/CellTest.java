package alo.jfx.controller;

import alo.jfx.model.Cell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CellTest {

    @DisplayName("Cell: Test setX and getX methods")
    @Test
    public void setXgetX() {
        // Given
        Cell cell = new Cell(0, 0);
        int expectedResult = 10;

        // When
        cell.setX(expectedResult);

        // Then
        assertEquals(expectedResult, cell.getX());
    }

    @DisplayName("Cell: Test setY and getY methods")
    @Test
    public void setYgetY() {
        // Given
        Cell cell = new Cell(0, 0);
        int expectedResult = 11;

        // When
        cell.setY(expectedResult);

        // Then
        assertEquals(expectedResult, cell.getY());
    }

    @DisplayName("Cell: Test equals between two cells")
    @Test
    public void equalTestBetweenTwoCells() {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(1, 1);

        assertEquals(cell1, cell2, "Cells are identical");

        Cell cell3 = new Cell(1, 2);

        assertNotEquals(cell1, cell3, "Cells are different");
    }

    @DisplayName("Cell: Test hash between two cells")
    @Test
    public void hashTest() {
        Cell cell1 = new Cell(1, 1);
        Cell cell2 = new Cell(1, 1);

        assertEquals(cell1.hashCode(), cell2.hashCode(), "Cells are identical");

        Cell cell3 = new Cell(3, 1);

        assertNotEquals(cell1, cell3, "Cells are different");
    }
}
