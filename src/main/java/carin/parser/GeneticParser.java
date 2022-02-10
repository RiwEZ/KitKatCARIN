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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticParser {
    private GameStates states;
    private final GeneticEntity host;
    private boolean action_flag;
    private final Map<String, Integer> var_map = new HashMap<>();
    private GeneticTokenizer tk;
    private final String path;
    private final Random rand;

    public GeneticParser(GeneticEntity host, String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        this.path = path;
        this.tk = new GeneticTokenizer(lines);
        this.host = host;
        this.rand = new Random();
    }

    public void evaluate() {
    }

    /** this should be use for testing only
     * @param expr list of 1 expression
     * @return Expr AST
     * @throws SyntaxError
     */
    public Expr testExpr(List<String> expr) throws Exception {
        if (expr.size() != 1) throw new Exception("wrong expr");
        tk = new GeneticTokenizer(expr);
        return parseExpr();
    }

    private String errInfo() {
        /*
            ....: err_msg at path:line
            line_str
                 ^ (arrow at pos)
        */
        return " at " + path + ':' + tk.getInfo();
    }

    // Program → Statement+
    private void parseProgram() throws SyntaxError {
        Statement a = parseStatement();
    }

    // Statement → Command | BlockStatement | IfStatement | WhileStatement
    private Statement parseStatement() throws SyntaxError {
        Statement p;
        //if (isBlock()) {}
        //if (isIf) {}
        //if (isWhile) {}
        if (Token.isCommand(tk.peek())) p = parseCommand();
        else throw new SyntaxError("", errInfo());
        return p;
    }

    // Command → AssignmentStatement | ActionCommand
    private Statement parseCommand() throws SyntaxError {
        Statement cmd;
        if (Token.isAssign(tk.peek())) {
            cmd = new Command(parseAssignment());
        }
        /*
        else if (Token.isAction(tk.peek())) {
        }
        */
        else throw new SyntaxError("", errInfo());
        return cmd;
    }

    // AssignmentStatement → <identifier> = Expression
    private Assignment parseAssignment() throws SyntaxError {
        String var = tk.consume().val();
        Expr v;
        if (tk.consume("="))
            v = parseExpr();
        else throw new SyntaxError("expression expected", errInfo());
        return new Assignment(var_map, var, v);
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
            tk.consume();
            v = new Factor(v, parsePower());
        }
        return v;
    }
    // Power → <number> | <identifier> | ( Expression ) | SensorExpression
    private Expr parsePower() throws SyntaxError {
        Expr v;
        if (!tk.hasNext()) throw new SyntaxError("expression expected", errInfo());
        else if (Token.isPower(tk.peek()))
            v = new Power(tk.consume(), rand);
        else if (Token.isSensor(tk.peek()))
            v = new SensorExpr(host, tk.consume().val(), states);
        else if (tk.peek("(")) {
            tk.consume("(");
            v = parseExpr();
            if (tk.peek().type() != Token.Type.DELIMITER)
                throw new SyntaxError("not a statement", errInfo());
            if (!tk.consume(")"))
                throw new SyntaxError("\")\" expected", errInfo());
        }
        else throw new SyntaxError("something wrong", errInfo());
        return v;
    }
}
