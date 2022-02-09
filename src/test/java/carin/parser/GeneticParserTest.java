package carin.parser;

import carin.entities.Antibody;
import carin.parser.ast.SyntaxError;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GeneticParserTest {

    @Test
    void expr_test() {
        GeneticParser parser;
        Map<String, Integer> var_map = new HashMap<>();
        try {
            parser = new GeneticParser(new Antibody(), "tests/expr1.in");
            System.out.print(parser.testExpr().evaluate(var_map));
        }
        catch (IOException | SyntaxError e) {e.printStackTrace();}
    }

}