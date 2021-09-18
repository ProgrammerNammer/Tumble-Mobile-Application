package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

public class ScoreSystem {
    private final double DEFAULT_SCORE_BASE;
    private double scoreMultiplier;

    public ScoreSystem() {
        this.DEFAULT_SCORE_BASE = 10;
        this.scoreMultiplier = 5;
    }

    public ScoreSystem(double defaultScoreMultiplier, double scoreMultiplier) {
        this.DEFAULT_SCORE_BASE = defaultScoreMultiplier;
        this.scoreMultiplier = scoreMultiplier;
    }

    public int convertToScore(final String VALID_WORD) {
        final int WORD_LENGTH = VALID_WORD.length();
        final int SHORTEST_WORD_LENGTH = 2;

        return (int) Math.floor(WORD_LENGTH * DEFAULT_SCORE_BASE * (scoreMultiplier * (WORD_LENGTH - SHORTEST_WORD_LENGTH)));
    }

    public void setScoreMultiplier(double score_multiplier) {
        this.scoreMultiplier = score_multiplier;
    }
}
