package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private int columns;
    private int rows;
    private Map<String, Symbol> symbols = new HashMap<>();

    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations = new HashMap<>();

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public Map<String, WinCombination> getWinCombinations() {
        return winCombinations == null ? new HashMap<>() : winCombinations;
    }
}
