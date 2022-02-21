package carin.util;

import carin.GameStates;
import carin.entities.IGeneticEntity;

import java.awt.geom.Point2D;
import java.util.Iterator;

public class SensorIterator implements Iterator<IGeneticEntity> {
    private final Point2D host;
    private final GameStates states;
    private int lookAt;

    public SensorIterator(Point2D host, GameStates states) {
        this.host = host;
        this.lookAt = 10;
        this.states = states;
    }

    private Point2D pos(double x, double y) {
        return new Point2D.Double(host.getX() + 36*x, host.getY() + 36*y);
    }

    private Point2D lookingPos() {
        int m = lookAt / 10;
        int i = lookAt % 10;

        return switch (i) {
            case 1 -> pos(0, -1*m);
            case 2 -> pos(m, -1*m);
            case 3 -> pos(m, 0);
            case 4 -> pos(m, m);
            case 5 -> pos(0, m);
            case 6 -> pos(-1*m, m);
            case 7 -> pos(-1*m, 0);
            case 8 -> pos(-1*m, -1*m);
            default -> null;
        };
    }

    public int getLookAt() {
        return lookAt;
    }

    @Override
    public boolean hasNext() {
        return lookAt == 10 || lookingPos() != null;
    }

    @Override
    public IGeneticEntity next() {
        if (!hasNext()) return null;
        lookAt++;
        Point2D p = lookingPos();

        while (lookAt != 0) {
            if ((lookAt % 10) > 8) {
                lookAt = lookAt + 10 - 8;
                p = lookingPos();
            }
            if (lookAt > 40) {
                lookAt = 0;
                return null;
            }

            if (states.isInMap(p) && !states.isUnOccupied(p)) break;
            lookAt++;
            p = lookingPos();
        }
        return states.entityMap().get(p);
    }
}
