package carin.entities;

import carin.GameStates;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;


public class Antibody extends GeneticEntity {
    private static int ID = 0;
    private final int antibodyID;

    public Antibody() {
        super("antibody");
        GeneticProgram code = new GeneticParser(GameStates.states(), this, "tests/antibody1.in").getProgram();
        this.setGeneticCode(code);
        this.antibodyID = ID;
        ID++;
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
