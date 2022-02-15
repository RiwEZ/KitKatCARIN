package carin.parser;

import carin.entities.GeneticEntity;
import carin.parser.ast.statements.StatementSeq;

import java.util.HashMap;
import java.util.Map;

public class GeneticProgram {
    private final StatementSeq seq;
    private final GeneticEntity host;
    private final Map<String, Integer> var_map;

    public GeneticProgram(StatementSeq seq, GeneticEntity host, Map<String, Integer> var_map) {
        this.seq = seq;
        this.var_map = var_map;
        this.host = host;
    }

    public GeneticProgram getCopy(GeneticEntity host) {
        return new GeneticProgram(seq, host, new HashMap<>());
    }

    public StatementSeq getStatementSeq() {
        return seq;
    }

    // this should be use for testing only
    public Map<String, Integer> getVarMap() {
        return var_map;
    }

    public void run() {
        seq.evaluate(var_map, host);
    }
}
