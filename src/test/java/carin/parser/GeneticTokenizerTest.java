package carin.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneticTokenizerTest {

    @Test
    void test1() throws IOException {
        Path p = Path.of("tests/genetic2.in");
        List<String> lines = Files.readAllLines(p);
        GeneticTokenizer tk = new GeneticTokenizer(lines);

        List<String> actual = new ArrayList<>();
        List<String> expeceted = new ArrayList<>(List.of("a", "=", "if", "while", "else", "+", "1"));

        int c = 0;
        while(tk.hasNext()) {
            actual.add(tk.consume().val());
            c++;
        }
        assertEquals(59, c);
        assertTrue(actual.containsAll(expeceted));
    }
}