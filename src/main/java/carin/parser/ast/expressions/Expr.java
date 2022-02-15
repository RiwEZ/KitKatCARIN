package carin.parser.ast.expressions;

import carin.entities.GeneticEntity;

import java.util.Map;

public interface Expr {

    /**
     * @return result of expression
     */
    int evaluate(Map<String, Integer> var_map, GeneticEntity host);
}
