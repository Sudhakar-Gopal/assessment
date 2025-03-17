package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {
    private String type;

    @JsonProperty("reward_multiplier") // Maps JSON "reward_multiplier" to Java "rewardMultiplier"
    private double rewardMultiplier;

    @JsonProperty("extra")
    private Integer extra;

    @JsonProperty("impact")
    private String impact;

    public String getType() {
        return type;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public Integer getExtra() {
        return extra;
    }

    public String getImpact() {
        return impact;
    }
}
