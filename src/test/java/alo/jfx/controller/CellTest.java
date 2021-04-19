package alo.jfx.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
