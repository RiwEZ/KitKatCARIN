package carin.parser.ast.expressions;

import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.GeneticEntity;
import carin.entities.Virus;
import java.util.Map;

public class SensorExpr implements Expr {
    private final String cmd;
    private final GameStates states;

    // maybe change cmd to expr
    public SensorExpr(String cmd, GameStates states) {
        this.cmd = cmd;
        this.states = states;
    }

    private int findVirus(GeneticEntity host) {
        for (GameStates.SensorIterator it = states.sensorIter(host.location()); it.hasNext(); ) {
            GeneticEntity e = it.next();
            if (e instanceof Virus) return it.getLookAt();
        }
        return 0;
    }

    private int findAntibody(GeneticEntity host) {
        for (GameStates.SensorIterator it = states.sensorIter(host.location()); it.hasNext(); ) {
            GeneticEntity e = it.next();
            if (e instanceof Antibody) return it.getLookAt();
        }
        return 0;
    }

    private int findNearby(GeneticEntity host) {
        for (GameStates.SensorIterator it = states.sensorIter(host.location()); it.hasNext(); ) {
            GeneticEntity e = it.next();
            if (e != null) return it.getLookAt();
        }
        return 0;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        return switch (cmd) {
            case "virus" -> findVirus(host);
            case "antibody" -> findAntibody(host);
            case "nearby" -> findNearby(host);
            default -> 0;
        };
    }
}
