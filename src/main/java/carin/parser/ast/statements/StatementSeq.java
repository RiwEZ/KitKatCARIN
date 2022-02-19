package carin.parser.ast.statements;

import carin.entities.IGeneticEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// StatementSeq → Statement*
public class StatementSeq implements Statement {
    private final List<Statement> sequences;

    public StatementSeq() {
        sequences = new LinkedList<>();
    }

    public void add(Statement statement) {
        sequences.add(statement);
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, IGeneticEntity host) {
        for (Statement st : sequences) {
            if (st.evaluate(var_map, host)) return true;
        }
        return false;
    }
}
