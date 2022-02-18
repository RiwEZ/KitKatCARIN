package carin.entities;

import java.awt.geom.Point2D;

public interface GeneticEntity {

    /**
     * @return location(x,y) of this GeneticEntity
     */
    Point2D location();

    /** move from host position (x, y)
     * @param x x position from host
     * @param y y position from host
     */
    void move(double x, double y);

    /**
     * run genetic code
     */
    void run();

}
