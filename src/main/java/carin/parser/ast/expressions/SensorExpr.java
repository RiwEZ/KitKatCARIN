package carin.parser.ast.expressions;

import carin.GameStates;
import carin.entities.GeneticEntity;

import java.util.Map;

public class SensorExpr implements Expr {
    private final String cmd;
    private final GameStates states;

    // maybe change cmd to expr
    public SensorExpr(String cmd, GameStates states) {
        this.cmd = cmd;
        this.states = states;
    }

    private int findVirus() {
        return 0;
    }
    private int findAntibody() {
        return 0;
    }
    private int findNearby() {
        return 0;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        switch (cmd) {
            case "virus":
                break;
            case "antibody":
                //
                break;
            case "nearby":
                //
                break;
            default:
        }
        return -1;
    }
}
