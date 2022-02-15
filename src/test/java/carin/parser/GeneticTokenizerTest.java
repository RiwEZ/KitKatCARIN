package carin.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneticTokenizerTest {

    @Test
    void token_is_correct() throws IOException {
        Path p = Path.of("tests/genetic2.in");
        List<String> lines = Files.readAllLines(p);
        GeneticTokenizer tk = new GeneticTokenizer(lines);

        List<String> actual = new ArrayList<>();
        List<String> expected = new ArrayList<>(List.of("a", "=", "if", "while", "else", "+", "1"));

        while(tk.hasNext()) actual.add(tk.consume().val());

        assertEquals(59, actual.size());
        assertTrue(actual.containsAll(expected));
    }
}