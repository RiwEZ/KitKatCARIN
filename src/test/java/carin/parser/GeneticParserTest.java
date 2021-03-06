package carin.parser;

import carin.entities.Antibody;
import org.junit.jupiter.api.DynamicTest;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GeneticParserTest {

    // should have expected result
    int[] expr_tester(String test, GeneticParser parser, Map<String, Integer> var_map) {
        List<String> l = new LinkedList<>();
        String[] arr = test.split("=");
        int expected;
        try {
            expected = Integer.parseInt(arr[1].trim());
        }
        catch (NumberFormatException ignored) {
            expected = -1;
        }
        if (parser != null) {
            try {
                l.add(arr[0].trim());
                int evalres = parser.testExpr(l).evaluate(var_map, null);
                System.out.println(evalres);
                return new int[]{evalres, expected};
            }
            catch (Exception e) {
                e.printStackTrace();
                if (expected == -1)
                    return new int[]{0, 0};
                else
                    return new int[]{0, 1};
            }
        }
        return null;
    }

    /*
        read test file of format
        expression = expected output

        when you need to debug add conditional breakpoint
        "test.equal("test_str"))" to expr_tester line
        SyntaxError will always tell line 0 because of how we're generating this test
    */
    @TestFactory
    Stream<DynamicTest> expr_test() {
        GeneticParser parser = null;
        Map<String, Integer> var_map = new HashMap<>();
        List<String> test_cases = new ArrayList<>();
        try {
            test_cases = Files.readAllLines(Path.of("tests/expression/expr1.in"));
            parser = new GeneticParser("tests/expression/expr1.in");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        GeneticParser finalParser = parser;
        return test_cases.stream().map(test -> dynamicTest(test, () -> {
            int[] res = expr_tester(test, finalParser, var_map);
            assertEquals(res[1], res[0]);
        }));
    }

    @Test
    void parser_simple_statement_test() throws SyntaxError {
        GeneticParser parser = new GeneticParser("tests/simple_statements.in");
        GeneticProgram program = parser.getProgram(new Antibody());
        program.run();
        GeneticProgram copy = program.getCopy(new Antibody());
        copy.run();

        Map<String, Integer> expected = Map.of(
                "a", 2, "b", -1, "t", 5, "c", 90
        );
        assertEquals(expected, program.getVarMap());
        assertEquals(expected, copy.getVarMap());
    }

    void parser_exception_tester(String path, String expected_msg) {
        GeneticParser parser = new GeneticParser(path);
        SyntaxError e = assertThrows(SyntaxError.class, () -> parser.getProgram(new Antibody()));
        assertTrue(e.getMessage().contains(expected_msg));
        System.out.println(e.getMessage());
    }

    @Test
    void parser_exception_test() {
        parser_exception_tester("tests/wrong_genetic/gene1.in", "at least 1 statement needed");
        parser_exception_tester("tests/wrong_genetic/gene2.in", "\"(\" expected");
        parser_exception_tester("tests/wrong_genetic/gene3.in", "\"then\" expected");
        parser_exception_tester("tests/wrong_genetic/gene4.in", "\"else\" expected");
        parser_exception_tester("tests/wrong_genetic/gene5.in", "expression expected");
        parser_exception_tester("tests/wrong_genetic/gene6.in", "direction expected");
        parser_exception_tester("tests/wrong_genetic/gene7.in", "\")\" expected");
        parser_exception_tester("tests/wrong_genetic/gene8.in", "statement expected");
        parser_exception_tester("tests/wrong_genetic/gene9.in", "direction expected");
    }
}