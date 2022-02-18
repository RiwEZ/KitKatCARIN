package carin.entities;

import java.awt.geom.Point2D;

/**
 * Representing empty space on entityMap.
 */
public class UnOccupied implements GeneticEntity {

    @Override
    public Point2D location() {
        return null;
    }

    @Override
    public void move(double x, double y) {
    }

    @Override
    public void run() {

    }
}
