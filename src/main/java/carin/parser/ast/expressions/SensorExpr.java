package carin.parser.ast.expressions;

import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.IGeneticEntity;
import carin.entities.Virus;
import java.util.Map;
import carin.util.SensorIterator;

public class SensorExpr implements Expr {
    private final String cmd;
    private final GameStates states;

    public SensorExpr(String cmd) {
        this.cmd = cmd;
        this.states = GameStates.states();
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
            if (e != null) {
                if (e instanceof Virus)
                    return 10*(it.getLookAt()/10)+1;
                else if (e instanceof Antibody)
                    return 10*(it.getLookAt()/10)+2;
            }
        }
        return 0;
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
