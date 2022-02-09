package carin.parser.ast.expressions;

import java.util.Map;

public interface Expr {

    /**
     * @return result of expression
     */
    public int evaluate(Map<String, Integer> var_map);
}
