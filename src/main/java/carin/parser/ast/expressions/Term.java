package carin.parser.ast.expressions;

import carin.entities.IGeneticEntity;

import java.util.Map;

public class Term implements Expr {
    private final Expr v;
    private final Expr u;
    private final Character op;

    public Term(Expr v, Character op, Expr u) {
        this.v = v;
        this.op =op;
        this.u = u;
    }

    @Override
    public int evaluate(Map<String, Integer> var_map, IGeneticEntity host) {
        return switch (op) {
            case '*' -> v.evaluate(var_map, host) * u.evaluate(var_map, host);
            case '/' -> v.evaluate(var_map, host) / u.evaluate(var_map, host);
            case '%' -> v.evaluate(var_map, host) % u.evaluate(var_map, host);
            default -> -1;
        };

    }
}
