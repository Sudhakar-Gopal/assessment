package game;

import model.GameConfig;
import java.util.Random;

public class MatrixGenerator {
    private final GameConfig config;
    private final Random random = new Random();

    public MatrixGenerator(GameConfig config) {
        this.config = config;
    }

    public String[][] generateMatrix() {
        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] matrix = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = getRandomSymbol();
            }
        }
        return matrix;
    }

    private String getRandomSymbol() {
        Object[] symbols = config.getSymbols().keySet().toArray();
        return (String) symbols[random.nextInt(symbols.length)];
    }
}
