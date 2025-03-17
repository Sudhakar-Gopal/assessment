package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinCombination {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;

    @JsonProperty("when")
    private String when;

    @JsonProperty("count")
    private int count;

    @JsonProperty("group")
    private String group;

    @JsonProperty("covered_areas") // Ensure mapping for covered_areas
    private String[][] coveredAreas;

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getWhen() {
        return when;
    }

    public int getCount() {
        return count;
    }

    public String getGroup() {
        return group;
    }

    public String[][] getCoveredAreas() {
        return coveredAreas;
    }
}
