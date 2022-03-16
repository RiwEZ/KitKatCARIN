package carin.entities;

import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.RenderType;

/**
 * Representing empty space on entityMap.
 */
public class UnOccupied extends Creature implements IGeneticEntity {

    public UnOccupied() {
        this.setRenderType(RenderType.NONE);
    }

    @Override
    public int getCurrHP() {
        return 0;
    }

    @Override
    public void setCurrHP(int hp) {
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
    public boolean getAttacked(IGeneticEntity entity, int dmg) {
        return false;
    }

    @Override
    public void setGeneticCode(GeneticProgram p) {
    }

    @Override
    public IGeneticEntity getCopy() {
        return null;
    }
}
