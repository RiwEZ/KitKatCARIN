package carin.entities;

import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.entities.IMobileEntity;

public interface IGeneticEntity extends IMobileEntity {

    /**
     * @return get current hp of this entity
     */
    int getCurrHP();

    /** move from host position (x,y)
     * @param x x position from host
     * @param y y position from host
     */
    void move(double x, double y);

    /**
     * run genetic code
     */
    void run();

    /** attack entity at (x,y) position from host
     * @param x x position from host
     * @param y y position from host
     * @return true if target die otherwise false
     */
    boolean attack(double x, double y);

    /**
     * @param dmg receiving dmg
     */
    void getAttacked(IGeneticEntity entity, int dmg);


    /**
     * @param p GeneticCode to use
     */
    void setGeneticCode(GeneticProgram p);

    /**
     * @return copy of this GeneticEntity with same GeneticCode
     */
    IGeneticEntity getCopy();
}
