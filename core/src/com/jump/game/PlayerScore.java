package com.jump.game;

public class PlayerScore {
    private String pseudo;
    private float distance;
    private int money;

    public PlayerScore(String pseudo, float distance, int money) {
        this.pseudo = pseudo;
        this.distance = distance;
        this.money = money;
    }
    public String getPseudo() { return pseudo; }
    public int getMoney() { return money; }
    public float getDistance() {return distance;}
}
