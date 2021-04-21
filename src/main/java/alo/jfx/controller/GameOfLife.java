package alo.jfx.controller;

public class GameOfLife {
    private static final int DEAD = 0;
    private static final int ALIVE = 1;

    private final int width;
    private final int height;

    private int[][] board;

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

        /* Testing neighborhood:
            1 2 3
            4 0 5
            6 7 8

            (0,0)   (1,0)   (2,0)
            (0,1)   (1,1)   (2,1)
            (0,2)   (1,2)   (2,2)

            (-1,-1)  (0,-1)  (+1,-1)
            (-1, 0)  (x, y)  (+1, 0)
            (-1,+1)  (0,+1)  (+1,+1)
         */

        Cell cell = new Cell(x, y);
        int nbNeighborSAlive = 0;

        nbNeighborSAlive += getNeighbor(cell, -1, -1);
        nbNeighborSAlive += getNeighbor(cell, -1, 0);
        nbNeighborSAlive += getNeighbor(cell, -1, 1);

        nbNeighborSAlive += getNeighbor(cell, 0, -1);
        nbNeighborSAlive += getNeighbor(cell, 0, 1);

        nbNeighborSAlive += getNeighbor(cell, 1, -1);
        nbNeighborSAlive += getNeighbor(cell, 1, 0);
        nbNeighborSAlive += getNeighbor(cell, 1, 1);

        return nbNeighborSAlive;
    }

    private int getNeighbor(Cell cell, int x, int y) {
        try {
            if (board[cell.getX() + x][cell.getY() + y] != 0)
                return 1;
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void updateBoard() {
        int[][] newBoard = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int nbNeighbors = getNbNeighborsAlive(x, y);

                if (board[x][y] == ALIVE) {
                    if (nbNeighbors == 2 || nbNeighbors == 3) {
                        newBoard[x][y] = ALIVE;
                    }
                } else {
                    if (nbNeighbors == 3) {
                        newBoard[x][y] = ALIVE;
                    }
                }

            }
        }

        board = newBoard;
    }
}
