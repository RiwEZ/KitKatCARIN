package carin.parser;

import carin.GameStates;
import carin.entities.IGeneticEntity;
import carin.parser.ast.expressions.*;
import carin.parser.ast.statements.*;
import de.gurkenlabs.litiengine.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

/**
 * Action and SensorExpr is depending on GameStates to init
 */
public class GeneticParser {
    private final Map<String, Integer> var_map = new HashMap<>();
    private GeneticTokenizer tk;
    private final String path;
    private final Random rand;

    public GeneticParser(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            this.tk = new GeneticTokenizer(lines);
        } catch (IOException e) {e.printStackTrace();}
        this.path = path.toString();
        this.rand = new Random();
    }

    public GeneticParser(String path) {
        this(Path.of(path));
    }

    public GeneticProgram getProgram(IGeneticEntity host) throws SyntaxError {
        if (!GameStates.states().isInit()) {
            Game.log().warning("at " + new Throwable().getStackTrace()[1] +
                    " GameStates didn't init, Action and SensorExpr will not work");
        }
        return new GeneticProgram(parseProgram(), host, var_map);
    }

    /** this should be use for testing only
     * @param expr list of 1 expression
     * @return Expr AST
     * @throws SyntaxError
     */
    Expr testExpr(List<String> expr) throws Exception {
        if (expr.size() != 1) throw new Exception("wrong expr");
        tk = new GeneticTokenizer(expr);
        Expr res = parseExpr(); // for inspecting AST in debugger
        return res;
    }

    private String errInfo() {
        /*
            ....: err_msg at path:line
            line_str
                 ^ (arrow at pos)
        */
        return " at " + path + tk.getInfo();
    }

    // Program → Statement+
    private StatementSeq parseProgram() throws SyntaxError {
        StatementSeq seq = new StatementSeq();
        if (!tk.hasNext()) throw new SyntaxError("at least 1 statement needed", errInfo());
        while (Token.isStatement(tk.peek())) {
            seq.add(parseStatement());
        }
        if (tk.hasNext()) throw new SyntaxError("statement expected", errInfo());
        return seq;
    }

    // ( Expression )
    private Expr parseCondition() throws SyntaxError {
        if (!tk.consume("(")) throw new SyntaxError("\"(\" expected", errInfo());
        Expr condition = parseExpr();
        if (!tk.consume(")")) throw new SyntaxError("\")\" expected", errInfo());
        return condition;
    }

    // Statement → Command | BlockStatement | IfStatement | WhileStatement
    private Statement parseStatement() throws SyntaxError {
        Statement p = new StatementSeq(); // empty statement for block
        if (Token.isCommand(tk.peek()))
            p = parseCommand();
        else if (Token.isBlock(tk.peek())) {
            // BlockStatement → { Statement* }
            tk.consume("{");
            StatementSeq seq = new StatementSeq();
            while (!tk.peek("}")) {
                seq.add(parseStatement());
            }
            tk.consume("}");
            p = seq;
        }
        else if (Token.isIf(tk.peek())) p = parseIf();
        else if (Token.isWhile(tk.peek())) {
            // WhileStatement → while ( Expression ) Statement
            tk.consume();
            Expr condition = parseCondition();
            Statement todo = parseStatement();
            p = new WhileStatement(condition, todo);
        }
        return p;
    }

    // IfStatement → if ( Expression ) then Statement else Statement
    private Statement parseIf() throws SyntaxError {
        tk.consume(); // consume if
        Expr condition = parseCondition();
        Statement then;
        Statement otherwise;
        if (tk.consume("then"))
            then = parseStatement();
        else throw new SyntaxError("\"then\" expected", errInfo());
        if (tk.consume("else"))
            otherwise = parseStatement();
        else throw new SyntaxError("\"else\" expected", errInfo());
        return new IfStatement(condition, then, otherwise);
    }

    /* Command → AssignmentStatement | ActionCommand
       AssignmentStatement → <identifier> = Expression
       ActionCommand → MoveCommand | AttackCommand
       MoveCommand → move Direction
       AttackCommand → shoot Direction
       Direction → left | right | up | down | upleft | upright | downleft | downright
    */
    private Statement parseCommand() throws SyntaxError {
        Statement cmd;
        if (Token.isAssign(tk.peek())) {
            String var = tk.consume().val();
            if (tk.consume("=")) {
                Expr v = parseExpr();
                cmd = new Assignment(var, v);
            }
            else throw new SyntaxError("expression expected", errInfo());
        }
        else if (Token.isAction(tk.peek())) {
            Token token = tk.consume();
            // parse Direction
            if (Token.isDirection(tk.peek())) {
                int direction = Token.getDirection(tk.consume().val());
                if (Token.isAttack(token)) cmd = new Action('a', direction);
                else cmd = new Action('m', direction);
            }
            else throw new SyntaxError("direction expected", tk.getInfo());
        }
        else throw new SyntaxError("something wrong", errInfo());
        return cmd;
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
                case "/" -> v = new Term(v, '/', parseFactor()); // let divide by zero be runtime exception
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
        if (Token.isNUM(tk.peek()))
            throw new SyntaxError("not a statement", errInfo());
        return v;
    }

    // Power → <number> | <identifier> | ( Expression ) | SensorExpression
    private Expr parsePower() throws SyntaxError {
        Expr v;
        if (!tk.hasNext()) throw new SyntaxError("expression expected", errInfo());
        else if (Token.isPower(tk.peek()))
            v = new Power(tk.consume(), rand);
        else if (Token.isSensor(tk.peek()))
            v = new SensorExpr(tk.consume().val());
        else if (tk.consume("(")) {
            v = parseExpr();
            if (!tk.consume(")"))
                throw new SyntaxError("\")\" expected", errInfo());
        }
        else throw new SyntaxError("something wrong", errInfo());
        return v;
    }
}
