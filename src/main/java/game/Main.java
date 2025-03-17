package game;

import model.GameConfig;
import model.Symbol;
import model.WinCombination;
import util.ConfigLoader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Main {
    
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java -jar scratch-game.jar --config <config.json> --betting-amount <amount>");
            return;
        }

        String configFile = args[1];
        int betAmount = Integer.parseInt(args[3]);

        try {
            GameConfig config = ConfigLoader.loadConfig(configFile);

            MatrixGenerator generator = new MatrixGenerator(config);
            WinningChecker checker = new WinningChecker(config);

            String[][] matrix = generator.generateMatrix();
            Map<String, List<String>> winningCombos = checker.checkWinningCombinations(matrix);

            double reward = calculateReward(betAmount, winningCombos, config.getSymbols(), config.getWinCombinations());

            String appliedBonusSymbol = null;
            if (!winningCombos.isEmpty()) {
                reward = BonusApplier.applyBonus(matrix, reward, config.getSymbols());
                appliedBonusSymbol = getAppliedBonusSymbol(matrix, config.getSymbols());
            } else {
                System.out.println("⚠️ No Winning Combinations - Bonus Symbols Will NOT Apply.");
            }

            // Print results


            System.out.println("Winning Combinations: " + winningCombos);
            System.out.println("Applied Bonus Symbol: " + (appliedBonusSymbol != null ? appliedBonusSymbol : "None"));
            System.out.println("\nFinal Reward: " + reward);

        } catch (IOException e) {
            System.out.println("Error loading config: " + e.getMessage());
        }
    }

    public static double calculateReward(int betAmount, Map<String, List<String>> winningResults, Map<String, Symbol> symbolsData, Map<String, WinCombination> winningCombinations) {
        double totalReward = 0;

        for (Map.Entry<String, List<String>> entry : winningResults.entrySet()) {
            String symbol = entry.getKey();

            if (!symbolsData.containsKey(symbol) || !"standard".equals(symbolsData.get(symbol).getType())) {
                System.out.println("Skipping Bonus Symbol in Reward Calculation: " + symbol);
                continue;
            }

            Symbol symbolData = symbolsData.get(symbol);
            double symbolMultiplier = symbolData.getRewardMultiplier();
            //System.out.println("🔹 Symbol: " + symbol + ", Multiplier: " + symbolMultiplier);

            if (symbolMultiplier == 0) {
                //System.out.println("ERROR: Symbol Multiplier for " + symbol + " is 0. Check config.json.");
                continue;
            }

            double symbolReward = betAmount * symbolMultiplier;

            for (String win : entry.getValue()) {
                if (!winningCombinations.containsKey(win)) {
                    //System.out.println("ERROR: Winning Combination Not Found: " + win);
                    continue;
                }
                double winMultiplier = winningCombinations.get(win).getRewardMultiplier();

                if (winMultiplier == 0) {
                    //System.out.println("ERROR: Winning Condition Multiplier for " + win + " is 0. Check config.json.");
                    continue;
                }

                System.out.println("🔹 Winning Condition: " + win + ", Multiplier: " + winMultiplier);
                symbolReward *= winMultiplier;
            }

            System.out.println("🔹 Symbol: " + symbol + ", Base Reward After Wins: " + symbolReward);
            totalReward += symbolReward;
        }

        System.out.println("\nTotal Reward Before Bonus: " + totalReward);
        return totalReward;
    }




    public static String getAppliedBonusSymbol(String[][] matrix, Map<String, Symbol> symbolsData) {
        for (String[] row : matrix) {
            for (String cell : row) {
                if (symbolsData.containsKey(cell) && "bonus".equals(symbolsData.get(cell).getType())) {
                    return cell;
                }
            }
        }
        return null;
    }
}
