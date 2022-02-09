package carin.parser.ast.expressions;

import java.util.Map;

public class Expression implements Expr {
    private Expr v, u;
    private Character op;

    public Expression(Expr v, Character op, Expr u) {
        this.v = v;
        this.op =op;
        this.u = u;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map) {
        return switch (op) {
            case '+' -> v.evaluate(var_map) + u.evaluate(var_map);
            case '-' -> v.evaluate(var_map) - u.evaluate(var_map);
            default -> -1;
        };

        // should throw
    }
}
