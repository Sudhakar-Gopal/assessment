

import game.WinningChecker;
import game.BonusApplier;
import game.Main;
import model.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScratchGameTest {
    private GameConfig config;
    private WinningChecker winningChecker;

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("config.json")).getFile());

        config = ConfigLoader.loadConfig(file.getAbsolutePath());
        winningChecker = new WinningChecker(config);
    }

    /**
     * ✅ **Test Case 1: No Winning Symbols (Final Reward = 0)**
     * Scenario: The matrix contains no winning combinations
     */
    @Test
    void testNoWinningSymbols() {
        String[][] matrix = {
                {"A", "B", "C", "D"},
                {"E", "F", "B", "D"},
                {"10x", "F", "+500", "G"},
                {"MISS", "E", "A", "+1000"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.isEmpty(), "Winning combinations should be empty for no-win scenario.");
    }

    @Test
    void testWinningCombinationForThreeSymbols() {
        String[][] matrix = {
                {"C", "C", "E", "D"},
                {"A", "C", "B", "F"},
                {"MISS", "D", "B", "+1000"},
                {"5x", "E", "A", "+500"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("C"), "C should be a winning symbol.");
        assertEquals(List.of("same_symbol_3_times"), winningCombinations.get("C"), "C should trigger same_symbol_3_times.");
    }


    @Test
    void testRewardCalculation() {
        Map<String, List<String>> winningCombinations = new HashMap<>();
        winningCombinations.put("C", List.of("same_symbol_3_times"));

        double reward = Main.calculateReward(100, winningCombinations, config.getSymbols(), config.getWinCombinations());

        assertEquals(100 * 2.5 * 1.0, reward, "Reward should be correctly calculated.");
    }

    @Test
    void testBonusApplication() {
        double baseReward = 200;
        String[][] matrix = {
                {"10x", "C", "E", "D"},
                {"A", "+1000", "B", "F"},
                {"MISS", "D", "B", "+500"},
                {"5x", "E", "A", "+500"}
        };

        double finalReward = BonusApplier.applyBonus(matrix, baseReward, config.getSymbols());
        assertEquals(2000, finalReward, "Reward should be multiplied by 10x.");
    }


    @Test
    void testMultipleWinningSymbols() {
        String[][] matrix = {
                {"E", "C", "E", "C"},
                {"C", "B", "B", "MISS"},
                {"F", "C", "F", "MISS"},
                {"+500", "E", "5x", "D"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);

        assertTrue(winningCombinations.containsKey("C"), "C should be a winning symbol.");
        assertTrue(winningCombinations.containsKey("E"), "E should be a winning symbol.");
        assertEquals(List.of("same_symbol_4_times"), winningCombinations.get("C"), "C should trigger same_symbol_4_times.");
        assertEquals(List.of("same_symbol_3_times"), winningCombinations.get("E"), "E should trigger same_symbol_3_times.");
    }

    @Test
    void testSameSymbolsHorizontally() {
        String[][] matrix = {
                {"C", "C", "C", "C"},
                {"A", "B", "D", "E"},
                {"B", "F", "A", "D"},
                {"F", "E", "A", "B"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("C"), "C should be detected as a horizontal win.");
        assertEquals(List.of("same_symbols_horizontally"), winningCombinations.get("C"), "C should trigger same_symbols_horizontally.");
    }


    @Test
    void testSameSymbolsVertically() {
        String[][] matrix = {
                {"D", "A", "B", "C"},
                {"D", "F", "B", "E"},
                {"D", "E", "F", "A"},
                {"D", "B", "A", "F"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("D"), "D should be detected as a vertical win.");
        assertEquals(List.of("same_symbols_vertically"), winningCombinations.get("D"), "D should trigger same_symbols_vertically.");
    }

    @Test
    void testSameSymbolsDiagonallyLTR() {
        String[][] matrix = {
                {"F", "A", "B", "C"},
                {"E", "F", "D", "B"},
                {"C", "B", "F", "E"},
                {"A", "B", "D", "F"}
        };

        Map<String, List<String>> winningCombinations = winningChecker.checkWinningCombinations(matrix);
        assertTrue(winningCombinations.containsKey("F"), "F should be detected as a diagonal win (↘).");
        assertEquals(List.of("same_symbols_diagonally_left_to_right"), winningCombinations.get("F"), "F should trigger same_symbols_diagonally_left_to_right.");
    }

}
