package carin.parser.ast.statements;

import java.util.LinkedList;
import java.util.List;

// Program → Statement+
// https://mitpress.mit.edu/sites/default/files/sicp/full-text/sicp/book/node126.html
public class StatementSeq implements Statement {
    private final List<Statement> sequences;

    public StatementSeq() {
        sequences = new LinkedList<>();
    }

    public void add(Statement statement) {
        sequences.add(statement);
    }

    public int size() {
        return sequences.size();
    }

    @Override
    public boolean evaluate() {
        for (Statement st : sequences) {
            if (st.evaluate()) return true;
        }
        return false;
    }
}
