package q1;

import java.util.Arrays;

public class GameMatrix {
    private boolean[][] matrix;
    private boolean[][] opsMatrix;
    private boolean isFrozen = false;

    public GameMatrix(int rows, int cols) {
        initMatrix(rows, cols);
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public boolean[][] getMatrix() {
        if (isFrozen) {
            throw new IllegalStateException("Matrix can't be accessed while in Frozen state");
        }

        return matrix;
    }

    public void toggleCellState(int row, int col) {
        matrix[row][col] = !matrix[row][col];
    }

    private void initMatrix(int rows, int cols) {
        matrix = new boolean[rows][cols];
        opsMatrix = new boolean[rows][cols];
        for (boolean[] booleans : matrix) {
            Arrays.fill(booleans, Boolean.FALSE);
        }
        for (boolean[] booleans : opsMatrix) {
            Arrays.fill(booleans, Boolean.FALSE);
        }
    }

    public void freeze() {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, opsMatrix[i], 0, matrix[i].length);
        }

        isFrozen = true;
    }

    public void unfreezeMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(opsMatrix[i], 0, matrix[i], 0, matrix[i].length);
        }

        isFrozen = false;
    }

    public void eatPixel(int row, int col) {
        opsMatrix[row][col] = false;
    }

    public boolean hasWhiteNeighbour(int row, int col) {
        boolean result = false;

        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, matrix.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, matrix[i].length - 1); j++) {
                if ((i != row || j != col) && !matrix[i][j]) {
                    result = true;
                }
            }
        }

        return result;
    }

    public void clear() {
        for (boolean[] booleans : matrix) {
            Arrays.fill(booleans, Boolean.FALSE);
        }
    }
}
