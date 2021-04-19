package alo.jfx.controller;

public class GameOfLife {
    private static final int DEAD = 0;
    private static final int ALIVE = 1;

    private final int width;
    private final int height;

    private final int[][] board;

    public GameOfLife(int width, int height) {
        this.width = width;
        this.height = height;

        board = new int[width][height];
    }

    public String getBoardString() {
        StringBuilder lineToPrint = new StringBuilder();

        for (int y = 0; y < height; y ++) {
            lineToPrint.append('|');

            for (int x = 0; x < width; x ++) {
                lineToPrint.append(getCell(x, y));
            }

            lineToPrint.append("|\n");
        }

        return lineToPrint.toString();
    }

    private Character getCell(int x, int y) {
        if (board[x][y] == DEAD)
            return '-';
        else
            return '*';
    }

    public void initCells(Cell ... cells) {
        for (Cell cell : cells) {
            try {
                board[cell.getX()][cell.getY()] = ALIVE;
            } catch (Exception e) {
                // ignore this cell
            }
        }
    }

    public void setAlive(int x, int y) {
        board[x][y] = ALIVE;
    }

    public void setDead(int x, int y) {
        board[x][y] = DEAD;
    }

    public int getState(int x, int y) {
        return board[x][y];
    }

    public int getNbNeighborsAlive(int x, int y) {




        return 0;
    }
}
