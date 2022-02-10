package carin.parser.ast.expressions;

import carin.parser.Token;

import java.util.Map;
import java.util.Random;

public class Power implements Expr {
    private final Token token;
    private final Random rand;

    public Power(Token token, Random rand) {
        this.token = token;
        this.rand = rand;
    }

    private int rand() {
        return rand.nextInt(100);
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
