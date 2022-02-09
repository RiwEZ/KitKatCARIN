package carin.parser.ast.expressions;

import carin.parser.Token;

import java.util.Map;

public class Power implements Expr {
    private final Token token;

    public Power(Token token) {
        this.token = token;
    }

    private int rand() {
        return (int) (Math.random() * 100) + 1;
    }
    @Override
    public int evaluate(Map<String, Integer> var_map) {
        if (Token.isNUM(token))
            return Token.toNumber(token);
        else if (Token.isIDENTIFIER(token)) {
            if (token.val().equals("random")) return rand();
            else if (var_map.get(token.val()) == null) return 0;
            return var_map.get(token.val());
        }

        return -1;
    }
}
