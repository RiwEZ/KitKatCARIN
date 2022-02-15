package carin.parser.ast.statements;

import carin.entities.GeneticEntity;
import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class WhileStatement implements Statement {
    private final Expr condition;
    private final Statement todo;

    public WhileStatement(Expr condition, Statement todo) {
        this.condition = condition;
        this.todo = todo;
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        int c = 0;
        while (condition.evaluate(var_map, host) > 0 && c <= 1000) {
            todo.evaluate(var_map, host);
            c++;
        }
        return false;
    }
}
