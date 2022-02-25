package carin.entities;

public class Player {
    private int currentCredit = 0; // default value is 0
    private static Player instance;

    public static Player instance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {}

    public void setCredit(int initCredit) {
        currentCredit = initCredit;
    }

    public int getCredit() {
        return currentCredit;
    }

    public void addCredit(int amount) {
        currentCredit += amount;
    }
}
