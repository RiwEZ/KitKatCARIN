package carin.parser.ast.statements;

import carin.parser.ast.expressions.Expr;

import java.util.Map;

public class Assignment implements Statement {
    private final String var;
    private final Expr val;
    private final Map<String, Integer> var_map;

    public Assignment(Map<String, Integer> var_map, String var, Expr val) {
        this.var = var;
        this.val = val;
        this.var_map = var_map;
    }

    @Override
    public void evaluate() {
        var_map.put(var, val.evaluate(var_map));
    }
}
