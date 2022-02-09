package carin.parser.ast.expressions;

import java.util.Map;

public class Factor implements Expr {
    private Expr v, u;

    public Factor(Expr v, Expr u) {
        this.v = v;
        this.u = u;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map) {
        return (int) Math.pow(v.evaluate(var_map), u.evaluate(var_map));
    }
}
