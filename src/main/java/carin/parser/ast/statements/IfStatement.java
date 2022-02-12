package carin.parser.ast.statements;

import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class IfStatement implements Statement {
    private final Expr condition;
    private final Statement then;
    private final Statement otherwise;
    private final Map<String, Integer> var_map;

    public IfStatement(Expr condition, Statement then, Statement otherwise, Map<String, Integer> var_map){
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
        this.var_map = var_map;
    }

    @Override
    public boolean evaluate() {
        if (condition.evaluate(var_map) > 0) then.evaluate();
        else otherwise.evaluate();
        return false;
    }
}
