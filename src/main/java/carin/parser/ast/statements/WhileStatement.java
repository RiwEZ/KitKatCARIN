package carin.parser.ast.statements;

import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class WhileStatement implements Statement {
    private final Expr condition;
    private final Statement todo;
    private final Map<String, Integer> var_map;

    public WhileStatement(Expr condition, Statement todo, Map<String, Integer> var_map) {
        this.condition = condition;
        this.todo = todo;
        this.var_map = var_map;
    }

    @Override
    public boolean evaluate() {
        int c = 0;
        while (condition.evaluate(var_map) > 0 && c <= 1000) {
            todo.evaluate();
            c++;
        }
        return false;
    }
}
