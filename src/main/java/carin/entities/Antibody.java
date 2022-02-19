package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;


public class Antibody extends GeneticEntity {
    private static int ID = 0;
    private final int antibodyID;
    private final int cred_gain = Config.antibody_cred_gain;

    public Antibody() {
        super("antibody");
        GeneticProgram code = new GeneticParser(GameStates.states(), this, "tests/genetic1.in").getProgram();
        this.setGeneticCode(code);
        this.antibodyID = ID;
        ID++;
    }

    @Override
    public boolean attack(double x, double y) {
        if (super.attack(x, y)) {
            states.player().changeCredit(cred_gain);
            return true;
        }
        return false;
    }

    @Override
    public void getAttacked(IGeneticEntity entity, int dmg) {
        super.getAttacked(entity, dmg);
        if (getCurrHP() == 0) {
            if (entity instanceof Virus) {
                IGeneticEntity copy = entity.getCopy();
                copy.setLocation(this.getLocation());
                states.addToSpawn(copy);
            }
        }
    }

    public int getID() {
        return antibodyID;
    }
}
