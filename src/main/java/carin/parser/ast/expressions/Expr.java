package carin.parser.ast.expressions;

import carin.entities.IGeneticEntity;

import java.util.Map;

public interface Expr {

    /**
     * @return result of expression
     */
    int evaluate(Map<String, Integer> var_map, IGeneticEntity host);
}
