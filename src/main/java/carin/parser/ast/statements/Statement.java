package carin.parser.ast.statements;

import carin.entities.GeneticEntity;

import java.util.Map;

public interface Statement {

    /**
     * @return true if one action have been made otherwise false
     */
    boolean evaluate(Map<String, Integer> var_map, GeneticEntity host);
}
