package carin.entities;

import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.entities.Creature;

import java.awt.geom.Point2D;

/**
 * Representing empty space on entityMap.
 */
public class UnOccupied extends Creature implements IGeneticEntity {

    @Override
    public int getCurrHP() {
        return 0;
    }

    @Override
    public void move(double x, double y) {
    }

    @Override
    public void run() {
    }

    @Override
    public boolean attack(double x, double y) {
        return false;
    }

    @Override
    public void getAttacked(IGeneticEntity entity, int dmg) {
    }

    @Override
    public void setGeneticCode(GeneticProgram p) {
    }

    @Override
    public IGeneticEntity getCopy() {
        return null;
    }
}
