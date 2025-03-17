package game;

import model.GameConfig;
import model.WinCombination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WinningChecker {
    private final GameConfig config;

    public WinningChecker(GameConfig config) {
        this.config = config;
    }

    public Map<String, List<String>> checkWinningCombinations(String[][] matrix) {
        System.out.println("\nGenerated Matrix:");
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        Map<String, List<String>> appliedCombinations = new HashMap<>();

        if (config.getWinCombinations() == null || config.getWinCombinations().isEmpty()) {
            return appliedCombinations;
        }

        checkLinearAndDiagonalPatterns(matrix, appliedCombinations);

       Map<String, Integer> symbolCounts = new HashMap<>();
        for (String[] row : matrix) {
            for (String cell : row) {
                symbolCounts.put(cell, symbolCounts.getOrDefault(cell, 0) + 1);
            }
        }

         for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();

            if (!config.getSymbols().containsKey(symbol) || !"standard".equals(config.getSymbols().get(symbol).getType())) {
                continue; // Skip bonus symbols
            }

           if (appliedCombinations.containsKey(symbol)) {
                continue;
            }

            String bestWinCombination = null;
            int bestCount = 0;

            for (Map.Entry<String, WinCombination> winEntry : config.getWinCombinations().entrySet()) {
                WinCombination rule = winEntry.getValue();

                if ("same_symbols".equals(rule.getWhen()) && count >= rule.getCount()) {
                    if (bestWinCombination == null || rule.getCount() > bestCount) {
                        bestWinCombination = winEntry.getKey();
                        bestCount = rule.getCount();
                    }
                }
            }

            if (bestWinCombination != null) {
                appliedCombinations.put(symbol, List.of(bestWinCombination)); // Apply only the highest rule
            }
        }

        return appliedCombinations;
    }


    private void checkLinearAndDiagonalPatterns(String[][] matrix, Map<String, List<String>> appliedCombinations) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (String[] strings : matrix) {
            if (allSame(strings)) {
                appliedCombinations.put(strings[0], List.of("same_symbols_horizontally"));
            }
        }

        for (int c = 0; c < columns; c++) {
            if (allSameColumn(matrix, c)) {
                appliedCombinations.put(matrix[0][c], List.of("same_symbols_vertically"));
            }
        }

        if (allSameDiagonalLTR(matrix)) {
            appliedCombinations.put(matrix[0][0], List.of("same_symbols_diagonally_left_to_right"));
        }

        if (allSameDiagonalRTL(matrix)) {
            appliedCombinations.put(matrix[0][columns - 1], List.of("same_symbols_diagonally_right_to_left"));
        }
    }


    private boolean allSame(String[] row) {
        String first = row[0];
        for (String cell : row) {
            if (!cell.equals(first)) return false;
        }
        return true;
    }


    private boolean allSameColumn(String[][] matrix, int column) {
        String first = matrix[0][column];
        for (int r = 1; r < matrix.length; r++) {
            if (!matrix[r][column].equals(first)) return false;
        }
        return true;
    }


    private boolean allSameDiagonalLTR(String[][] matrix) {
        String first = matrix[0][0];
        for (int i = 1; i < matrix.length; i++) {
            if (!matrix[i][i].equals(first)) return false;
        }
        return true;
    }



    private boolean allSameDiagonalRTL(String[][] matrix) {
        int size = matrix.length;
        String first = matrix[0][size - 1];

        for (int i = 1; i < size; i++) {
            if (!matrix[i][size - 1 - i].equals(first)) return false;
        }
        return true;
    }
}
