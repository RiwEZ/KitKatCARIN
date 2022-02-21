package carin.entities;

import carin.Config;

public class Player {
    private int currentCredit = Config.initial_credits;
    private static Player instance;

    public static Player instance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {}

    public int getCredit() {
        return currentCredit;
    }

    public void addCredit(int amount) {
        currentCredit += amount;
    }
}
