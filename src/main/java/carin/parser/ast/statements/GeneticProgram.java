package carin.parser.ast.statements;

import java.util.HashMap;
import java.util.Map;

public class GeneticProgram implements Statement {
    private final StatementSeq seq;
    private final Map<String, Integer> var_map;

    public GeneticProgram(StatementSeq seq, Map<String, Integer> var_map) {
        this.seq = seq;
        this.var_map = var_map;
    }

    public GeneticProgram getCopy() {
        return new GeneticProgram(seq, new HashMap<>());
    }

    // this should be use for testing only
    public Map<String, Integer> getVarMap() {
        return var_map;
    }

    @Override
    public boolean evaluate() {
        return seq.evaluate();
    }
}
