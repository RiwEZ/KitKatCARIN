package carin.parser.ast.statements;

import carin.entities.GeneticEntity;
import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class Assignment implements Statement {
    private final String var;
    private final Expr val;

    public Assignment(String var, Expr val) {
        this.var = var;
        this.val = val;
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        var_map.put(var, val.evaluate(var_map, host));
        return false;
    }
}
