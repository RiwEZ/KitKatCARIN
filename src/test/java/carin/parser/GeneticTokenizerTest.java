package carin.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class GeneticTokenizerTest {

    @Test
    void test1() throws IOException {
        Path p = Path.of("tests/genetic2.in");
        List<String> lines = Files.readAllLines(p);

        GeneticTokenizer tk = new GeneticTokenizer(lines);
        while(tk.hasNext())
            System.out.println(tk.consume());
    }
}