package carin.parser;

import carin.GameStates;
import carin.entities.GeneticEntity;
import carin.parser.ast.SyntaxError;
import carin.parser.ast.expressions.*;
import carin.parser.ast.statements.Assignment;
import carin.parser.ast.statements.Command;
import carin.parser.ast.statements.Statement;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GeneticParser {
    private GameStates states;
    private final GeneticEntity host;
    private boolean action_flag;
    private Map<String, Integer> var_map;
    private GeneticTokenizer tk;

    public GeneticParser(GeneticEntity host, String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        this.tk = new GeneticTokenizer(lines);
        this.host = host;
    }

    public void evaluate() {
    }

    public Expr testExpr() throws SyntaxError {
        return parseExpr();
    }

    // Program → Statement+
    private void parseProgram() throws SyntaxError {
        Statement a = parseStatement();
    }

    // Statement → Command | BlockStatement | IfStatement | WhileStatement
    private Statement parseStatement() throws SyntaxError {
        //if (isBlock()) {}
        //if (isIf) {}
        //if (isWhile) {}
        if (Token.isCommand(tk.peek())) {
            parseCommand();
        }
        return null;
    }

    private Statement parseCommand() throws SyntaxError {
        Statement cmd;
        if (Token.isAssign(tk.peek())) {
            cmd = new Command(parseAssignment());
            return cmd;
        }
        else if (Token.isAction(tk.peek())) {

        }
        return null;
    }

    private Assignment parseAssignment() throws SyntaxError {
        String var = tk.consume().val();
        Expr expr = null;
        if (tk.consume("=")) {
            expr = parseExpr();
        }
        //else throw
        return new Assignment(var_map, var, expr);
    }

    // Expression → Term (+Term)* | Term (-Term)*
    private Expr parseExpr() throws SyntaxError {
        Expr v = parseTerm();
        while (tk.peek("+") || tk.peek("-")) {
            String op = tk.consume().val();
            switch (op) {
                case "+" -> v = new Expression(v, '+', parseTerm());
                case "-" -> v = new Expression(v, '-', parseTerm());
            }
        }
        return v;
    }
    // Term → Factor (*Factor)* | Factor (/Factor)* | Factor (%Factor)*
    private Expr parseTerm() throws SyntaxError {
        Expr v = parseFactor();
        while (tk.peek("*") || tk.peek("/") || tk.peek("%")) {
            String op = tk.consume().val();
            switch (op) {
                case "*" -> v = new Term(v, '*', parseFactor());
                case "/" -> v = new Term(v, '/', parseFactor());
                case "%" -> v = new Term(v, '%', parseFactor());
            }
        }
        return v;
    }
    // Factor → Power (^Power)*
    private Expr parseFactor() throws SyntaxError {
        Expr v = parsePower();
        while (tk.peek("^")) {
            String op = tk.consume().val();
            v = new Factor(v, parsePower());
        }
        return v;
    }
    // Power → <number> | <identifier> | ( Expression ) | SensorExpression
    private Expr parsePower() throws SyntaxError {
        Expr v;
        if (!tk.hasNext()) throw new SyntaxError("Something wrong with Power", tk.getInfo());
        else if (Token.isPower(tk.peek()))
            v = new Power(tk.consume());
        else if (Token.isSensor(tk.peek()))
            v = new SensorExpr(host, tk.consume().val(), states);
        else if (tk.peek("(")) {
            tk.consume("(");
            v = parseExpr();
            tk.consume(")");
        }
        else throw new SyntaxError("Something wrong with Power", tk.getInfo());
        return v;
    }
}
