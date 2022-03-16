package carin.entities;

import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;
import carin.parser.SyntaxError;

import java.nio.file.Path;

public class Virus extends GeneticEntity {
    private static int ID = 0;
    private final int virusID;

    public Virus() {
        this("");
    }

    public Virus(String name) {
        super("virus", name);
        super.setSprite("virus1");
        this.virusID = ID;
        ID++;
    }

    /**
     * @param name name of virus
     * @param path path to genetic code
     */
    public Virus(String name, Path path) {
        this(name);

        GeneticProgram code = null;
        try {
            code = new GeneticParser(path).getProgram(this);
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        this.setGeneticCode(code);
    }

    public int getID() {
        return virusID;
    }
}
