package carin.parser;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

// https://en.wikipedia.org/wiki/Lexical_analysis
// https://www.cs.man.ac.uk/~pjj/farrell/comp3.html#:~:text=The%20tokenizer%20is%20responsible%20for,compiler%20is%20called%20the%20Parser.&text=The%20lexer%20and%20parser%20together,as%20the%20compiler's%20front%20end.
public class GeneticTokenizer {
    private final List<String> lines;
    private String next;
    private int pos;
    private int line;
    private Set<String> reserved = new HashSet<>(List.of(
        "antibody", "down", "downleft", "downright", "else", "if",
        "left", "move", "nearby", "right", "shoot", "then", "up",
        "upleft", "upright", "virus", "while"
    ));
    private Set<String> operators = new HashSet<>(List.of(
            "+", "-", "=", "%", "*", "/"
    ));
    private Set<Character> delimeters = new HashSet<>(List.of(
            '{', '}', '(', ')', ' '
    ));

    public GeneticTokenizer(List<String> lines) {
        this.lines = lines;
        this.pos = 0;
        this.line = 0;
        this.computeNext();
    }

    private boolean notDelimeter(char c) {
        return !delimeters.contains(c);
    }

    private boolean isReserved(String str) {
        return reserved.contains(str);
    }

    private boolean isOperator(String str) {
        return operators.contains(str);
    }
    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // AssignmentStatement → <identifier> = Expression
    // IfStatement → if ( Expression ) then Statement else Statement
    // BlockStatement → { Statement* }
    public void computeNext() {
        StringBuilder s = new StringBuilder(); // read_str
        String curr_line = lines.get(line);
        next = "";

        // find next token
        while (curr_line.charAt(pos) == ' ') {
            pos++;
            // check if end of line
            if (pos == curr_line.length()) {
                if (line == lines.size() - 1) return; // eof
                line += 1;
                curr_line = lines.get(line);
                pos = 0;
            }
        }

        s.append(curr_line.charAt(pos));
        pos++;
        // ignore comment
        if (s.toString().equals("#")) {
            line += 1;
            pos = 0;
            return;
        }
        if (isOperator(s.toString())) {
            next = s.toString();
            return;
        }
        while (notDelimeter(curr_line.charAt(pos))) {
            s.append(curr_line.charAt(pos));
            pos++;
        }

        // determine token type
        if (isNumber(s.toString())) {
            next = s.toString();
            return;
        }
        next = s.toString();
    }


    public void peek() {

    }

    public void consume() {

    }

}
