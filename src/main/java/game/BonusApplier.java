package game;

import model.Symbol;
import java.util.Map;


public class BonusApplier {

    public static double applyBonus(String[][] matrix, double reward, Map<String, Symbol> symbolsData) {
        if (reward == 0) {
            //logger.info("Wins Detected - Bonus Symbols Will NOT Apply.");
            return reward;
        }

        String appliedBonus = null;
        for (String[] row : matrix) {
            for (String cell : row) {
                if (symbolsData.containsKey(cell) && "bonus".equals(symbolsData.get(cell).getType())) {
                    if (appliedBonus == null) {
                        appliedBonus = cell;
                        Symbol bonusSymbol = symbolsData.get(cell);

                        if ("multiply_reward".equals(bonusSymbol.getImpact())) {
                            reward *= bonusSymbol.getRewardMultiplier();
                        } else if ("extra_bonus".equals(bonusSymbol.getImpact())) {
                            reward += bonusSymbol.getExtra();
                        }
                        break;
                    }
                }
            }
        }

        // logger.info("🔹 Applied Bonus Symbol: " + appliedBonus);
        //logger.info("🔹 Final Reward After Bonus: " + reward);
        return reward;
    }
}
