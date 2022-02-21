package carin.entities;

import carin.Config;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;
import carin.parser.SyntaxError;
import de.gurkenlabs.litiengine.entities.CollisionInfo;

import java.nio.file.Path;

@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = false)
public class Antibody extends GeneticEntity {
    private static int ID = 0;
    private final int antibodyID;
    private final int cred_gain = Config.antibody_cred_gain;

    public Antibody() {
        this("");
    }

    public Antibody(String name) {
        super("antibody", name);
        this.antibodyID = ID;
        ID++;
    }

    /**
     * @param name name of antibody
     * @param path path to genetic code
     */
    public Antibody(String name, Path path) {
        this(name);

        GeneticProgram code = null;
        try {
            code = new GeneticParser(path).getProgram(this);
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        this.setGeneticCode(code);
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
