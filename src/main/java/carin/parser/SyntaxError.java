package carin.parser;

public class SyntaxError extends Exception {
    public SyntaxError(String message, String info) {
        super(message + info);
    }
}
