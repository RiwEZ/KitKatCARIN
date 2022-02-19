package carin.entities;

import carin.GameStates;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;

public class Virus extends GeneticEntity {
    private static int ID = 0;
    private final int virusID;

    public Virus() {
        super("virus");
        GeneticProgram code = new GeneticParser(GameStates.states(), this, "tests/virus1.in").getProgram();
        this.setGeneticCode(code);
        this.virusID = ID;
        ID++;
    }

    public int getID() {
        return virusID;
    }
}
