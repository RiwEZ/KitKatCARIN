package carin.parser;

import carin.entities.IGeneticEntity;
import carin.parser.ast.statements.StatementSeq;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GeneticProgram {
    private final StatementSeq seq;
    private final IGeneticEntity host;
    private final Map<String, Integer> var_map;

    public GeneticProgram(StatementSeq seq, IGeneticEntity host, Map<String, Integer> var_map) {
        this.seq = seq;
        this.var_map = var_map;
        this.host = host;
    }

    public GeneticProgram getCopy(IGeneticEntity host) {
        return new GeneticProgram(seq, host, new HashMap<>());
    }

    public StatementSeq getStatementSeq() {
        return seq;
    }

    public Map<String, Integer> getVarMap() {
        return Collections.unmodifiableMap(var_map);
    }

    public void run() {
        seq.evaluate(var_map, host);
    }
}
