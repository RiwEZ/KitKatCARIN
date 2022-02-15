package carin.parser.ast.statements;

import carin.entities.GeneticEntity;
import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class IfStatement implements Statement {
    private final Expr condition;
    private final Statement then;
    private final Statement otherwise;

    public IfStatement(Expr condition, Statement then, Statement otherwise){
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        if (condition.evaluate(var_map, host) > 0) then.evaluate(var_map, host);
        else otherwise.evaluate(var_map, host);
        return false;
    }
}
