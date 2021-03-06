package carin.parser;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

// https://en.wikipedia.org/wiki/Lexical_analysis
// https://www.cs.man.ac.uk/~pjj/farrell/comp3.html#:~:text=The%20tokenizer%20is%20responsible%20for,compiler%20is%20called%20the%20Parser.&text=The%20lexer%20and%20parser%20together,as%20the%20compiler's%20front%20end.
public class GeneticTokenizer {
    private final List<String> lines;
    private String next;
    private Token.Type nextType;
    private int pos;
    private int line;
    private final Set<String> reserved = Token.getReserved();
    private final Set<String> operators = new HashSet<>(List.of(
            "+", "-", "=", "%", "*", "/"
    ));
    private final Set<Character> delimeters = new HashSet<>(List.of(
            '{', '}', '(', ')'
    ));

    public GeneticTokenizer(List<String> lines) {
        this.lines = lines;
        this.pos = 0;
        this.line = 0;
        this.computeNext();
    }

    private boolean notDelimeter(char c) {
        return !delimeters.contains(c) && !isOperator(String.valueOf(c));
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

    public void computeNext() {
        if (lines.size() == 0) return;

        StringBuilder s = new StringBuilder(); // read_str
        String curr_line = lines.get(line);
        next = "";
        nextType = null;

        // find next token
        while (pos >= curr_line.length() || curr_line.charAt(pos) == ' ') {
            pos++;
            // check if end of line
            if (pos >= curr_line.length()) {
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
            if (line < lines.size()) computeNext();
            return;
        }
        // type OP
        if (isOperator(s.toString())) {
            next = s.toString();
            nextType = Token.Type.OP;
            return;
        }
        // type DELIMITER
        if (!notDelimeter(s.charAt(0))) {
            next = s.toString();
            nextType = Token.Type.DELIMITER;
            return;
        }
        // add character to token until can't
        while (pos < curr_line.length() && notDelimeter(curr_line.charAt(pos)) && curr_line.charAt(pos) != ' ') {
            s.append(curr_line.charAt(pos));
            pos++;
        }
        // type NUM
        if (isNumber(s.toString())) {
            next = s.toString();
            nextType = Token.Type.NUM;
            return;
        }
        // type RESERVED
        if (isReserved(s.toString())) {
            next = s.toString();
            nextType = Token.Type.RESERVED;
            return;
        }
        next = s.toString(); // type IDENTIFIER
        nextType = Token.Type.IDENTIFIER;
    }

    public boolean hasNext() {
        return next != null && !next.equals("");
    }

    public String getInfo() {
        if (lines.size() == 0) return "";
        if (line < lines.size())
            return ":" + line + "\n\t" + lines.get(line) + "\n\t" + " ".repeat(pos - 1) + '^';
        return ":unknown";
    }

    public Token peek() {
        return new Token(next, nextType);
    }

    public Token consume() {
        Token res = new Token(next, nextType);
        computeNext();
        return res;
    }

    public boolean peek(String str) {
        if (!hasNext()) return false;
        return next.equals(str);
    }

    public boolean consume(String str) {
        if (peek(str)) {
            consume();
            return true;
        }
        else return false;
    }

}
