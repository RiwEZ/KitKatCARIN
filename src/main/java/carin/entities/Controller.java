package carin.entities;

import carin.GameStates;
import de.gurkenlabs.litiengine.entities.Creature;

import java.awt.geom.Point2D;

public class Controller {

    private final Creature entity;
    private final GameStates states;

    public Controller(Creature entity, GameStates states){
        this.entity = entity;
        this.states = states;
    }

    public void move(double x, double y){
        Point2D pos = new Point2D.Double(this.entity.getX()+(x*36), this.entity.getY()+(y*36));
        if (states.isInMap(pos)) {
            Point2D prevPos = new Point2D.Double(this.entity.getX(), this.entity.getY());

            if (states.isUnOccupied(pos)) {
                states.entityMap().put(pos, (GeneticEntity) this.entity);
                states.entityMap().put(prevPos, states.unOccupied());
                this.entity.setLocation(pos);
            }

        }
    }
}
