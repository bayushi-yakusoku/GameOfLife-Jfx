package alo.jfx.model;

public class GameOfLife {

    private static final char CHAR_DEAD = '-';
    private static final char CHAR_ALIVE = '*';

    private static final int DEAD = 0;
    private static final int ALIVE = 1;

    private final int width;
    private final int height;

    private int[][] board;

    /**
     * Create a Game of life.
     * Its grid will be of size Width x Height cells
     *
     * @param width:  number of cells per line
     * @param height: number of cells per column
     */
    public GameOfLife(int width, int height) {
        this.width = width;
        this.height = height;

        board = new int[width][height];
    }

    /**
     * Get a string representing the game of life grid (with some beautiful borders...).
     * Cells will be represented by a single char.
     * Living cell with '{@value CHAR_ALIVE}' and Dead cell with '{@value CHAR_DEAD}'
     *
     * @return String representing the whole Game of lif board
     */
    public String getBoardString() {
        StringBuilder lineToPrint = new StringBuilder();

        for (int y = 0; y < height; y++) {
            lineToPrint.append('|');

            for (int x = 0; x < width; x++) {
                lineToPrint.append(getCell(x, y));
            }

            lineToPrint.append("|\n");
        }

        return lineToPrint.toString();
    }

    private Character getCell(int x, int y) {
        if (board[x][y] == DEAD)
            return CHAR_DEAD;
        else
            return CHAR_ALIVE;
    }

    /**
     * Add living cells to the Game of life
     *
     * @param cells {@link Cell} list of expected living cells
     */
    public void initCells(Cell... cells) {
        for (Cell cell : cells) {
            try {
                board[cell.getX()][cell.getY()] = ALIVE;
            } catch (Exception e) {
                // ignore this cell
            }
        }
    }

    /**
     * Set a cell, identified by its coordinates, as a living one
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public void setAlive(int x, int y) {
        board[x][y] = ALIVE;
    }

    /**
     * Set a cell, identified by its coordinates, as a dead one
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public void setDead(int x, int y) {
        board[x][y] = DEAD;
    }

    /**
     * Set a cell, identified by its coordinates, at a particular state
     *
     * @param x     horizontal coordinate
     * @param y     vertical coordinate
     * @param state new state for the cell
     */
    public void setState(int x, int y, int state) {
        board[x][y] = state;
    }

    /**
     * Alternate the state of the cell, identified by its coordinates,
     * between Dead and Alive and return the new state
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return state of the cell after the alternate operation
     */
    public int alternateState(int x, int y) {
        if (board[x][y] == ALIVE)
            board[x][y] = DEAD;
        else
            board[x][y] = ALIVE;

        return board[x][y];
    }

    /**
     * Get the value of a cell identified by its coordinates
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return cell's value
     */
    public int getState(int x, int y) {
        return board[x][y];
    }

    /**
     * Get the number of living cells around the one (neighbors) identified by its coordinates
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return number of living cells in the neighborhood
     */
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

    /**
     * Get the board used to store the Game of life
     *
     * @return board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Apply the Game of life rules for a single turn
     */
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
