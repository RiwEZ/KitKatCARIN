package carin.entities;

import carin.GameStates;
import de.gurkenlabs.litiengine.entities.Creature;

import java.awt.geom.Point2D;

public class Controller {

    public static final int minX = GameStates.minX, maxX = GameStates.maxX;
    public static final int maxY = GameStates.maxY, minY = GameStates.minY;

    private final Creature entity;

    public Controller(Creature entity){
        this.entity = entity;
    }

    private boolean isInMap(double x, double y) {
       return (this.entity.getX() + x*36 >= minX)
               && (this.entity.getX() + x*36 <= maxX)
               && (this.entity.getY() + y*36 <= minY)
               && (this.entity.getY() + y*36 >= maxY);
    }

    public void move(double x, double y){
        if (isInMap(x, y)) {
            Point2D prevPos = new Point2D.Double(this.entity.getX(), this.entity.getY());
            Point2D pos = new Point2D.Double(this.entity.getX()+(x*36), this.entity.getY()+(y*36));

            if (GameStates.isUnOccupied(pos)) {
                GameStates.getEntityMap().put(pos, (GeneticEntity) this.entity);
                GameStates.getEntityMap().put(prevPos, GameStates.unOccupied());
                this.entity.setLocation(pos);
            }

        }
    }
}
