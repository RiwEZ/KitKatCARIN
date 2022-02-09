package carin.parser.ast;

public class SyntaxError extends Exception {
    public SyntaxError(String message, String info) {
        super(message + "at line " + info);
    }
}
