package carin.parser.ast.expressions;

import carin.entities.IGeneticEntity;

import java.util.Map;

public class Factor implements Expr {
    private Expr v, u;

    public Factor(Expr v, Expr u) {
        this.v = v;
        this.u = u;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map, IGeneticEntity host) {
        return (int) Math.pow(v.evaluate(var_map, host), u.evaluate(var_map, host));
    }
}
