package carin.entities;

import carin.Config;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;
import carin.parser.SyntaxError;
import carin.util.SoundManager;

import java.awt.geom.Point2D;
import java.nio.file.Path;
import java.util.Map;

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
            Player.instance().addCredit(cred_gain);
            return true;
        }
        return false;
    }

    @Override
    public void getAttacked(IGeneticEntity entity, int dmg) {
        super.getAttacked(entity, dmg);
        if (this.isDead()) {
            if (entity instanceof Virus) {
                IGeneticEntity copy = entity.getCopy();
                copy.setLocation(this.getLocation());
                states.addToSpawn(copy);
            }
        }
    }

    public void manualMove(Point2D prevPos, Point2D pos) {
        Map<Point2D, IGeneticEntity> entityMap = states.entityMap();
        IGeneticEntity unoccupied = states.unOccupied();
        IGeneticEntity atSnap = entityMap.get(pos);
        int hp_cost = Config.move_hp_cost;
        if (super.getCurrHP() > hp_cost && atSnap != null && atSnap.equals(unoccupied)) {
            super.setCurrHP(super.getCurrHP() - hp_cost);
            SoundManager.moveSound();
            setLoc(prevPos, pos);
            return;
        }
        this.setLocation(prevPos);
    }

    public int getID() {
        return antibodyID;
    }
}
