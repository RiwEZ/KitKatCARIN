package carin.parser.ast;

import carin.GameStates;
import carin.entities.GeneticEntity;

public class SensorExpr {
    private final String cmd;
    private final GeneticEntity host;
    private final GameStates states;

    // maybe change cmd to expr
    public SensorExpr(GeneticEntity host, String cmd, GameStates states) {
        this.host = host;
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

    public int evaluate() throws SyntaxError {
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
                throw new SyntaxError("Invalid SensorExpression at line"); // + line ...
        }
        return 0;
    }
}
