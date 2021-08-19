package com.mobdeve.s18.bautista_and_burguillos.tumble_app;

public class ScoreboardItem {
    private String score;
    private String username;
    public ScoreboardItem (String Username,String score) {
        this.score = score;
        username = Username;
    }

    public String getScore() {
        return score;
    }
    public void setScore(String Score) {
        score= Score;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String Username) {
        username= Username;
    }
}
