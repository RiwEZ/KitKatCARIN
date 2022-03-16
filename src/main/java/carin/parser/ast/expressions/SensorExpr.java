package carin.parser.ast.expressions;

import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.IGeneticEntity;
import carin.entities.Virus;

import java.awt.geom.Point2D;
import java.util.Map;
import carin.util.SensorIterator;

public class SensorExpr implements Expr {
    private final String cmd;
    private final GameStates states;
    private int direction;

    public SensorExpr(String cmd) {
        this.cmd = cmd;
        this.states = GameStates.states();
    }

    public SensorExpr(String cmd, int direction) {
        this(cmd);
        this.direction = direction;
    }

    private int findVirus(IGeneticEntity host) {
        for (SensorIterator it = states.sensorIter(host.getLocation()); it.hasNext(); ) {
            IGeneticEntity e = it.next();
            if (e instanceof Virus) return it.getLookAt();
        }
        return 0;
    }

    private int findAntibody(IGeneticEntity host) {
        for (SensorIterator it = states.sensorIter(host.getLocation()); it.hasNext(); ) {
            IGeneticEntity e = it.next();
            if (e instanceof Antibody) return it.getLookAt();
        }
        return 0;
    }

    private int findNearby(IGeneticEntity host) {
        for (SensorIterator it = states.sensorIter(host.getLocation()); it.hasNext(); ) {
            IGeneticEntity e = it.next();
            if (e != null && posIsCorrect(host.getLocation(), e.getLocation())) {
                if (e instanceof Virus)
                    return 10*(it.getLookAt()/10)+1;
                else if (e instanceof Antibody)
                    return 10*(it.getLookAt()/10)+2;
            }
        }
        return 0;
    }

    private boolean posIsCorrect(Point2D hostLoc, Point2D pos) {
        // direction from NUMPAD
        return switch (direction) {
            case 1 -> hostLoc.getY() < pos.getY() && hostLoc.getX() < pos.getX();
            case 2 -> hostLoc.getY() < pos.getY() && hostLoc.getX() == pos.getX();
            case 3 -> hostLoc.getY() < pos.getY() && hostLoc.getX() > pos.getX();
            case 4 -> hostLoc.getY() == pos.getY() && hostLoc.getX() > pos.getX();
            case 6 -> hostLoc.getY() == pos.getY() && hostLoc.getX() < pos.getX();
            case 7 -> hostLoc.getY() > pos.getY() && hostLoc.getX() < pos.getX();
            case 8 -> hostLoc.getY() > pos.getY() && hostLoc.getX() == pos.getX();
            case 9 -> hostLoc.getY() > pos.getY() && hostLoc.getX() > pos.getX();
            default -> false;
        };
    }

    @Override
    public int evaluate(Map<String, Integer> var_map, IGeneticEntity host) {
        return switch (cmd) {
            case "virus" -> findVirus(host);
            case "antibody" -> findAntibody(host);
            case "nearby" -> findNearby(host);
            default -> 0;
        };
    }
}
