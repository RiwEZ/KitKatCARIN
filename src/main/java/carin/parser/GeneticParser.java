package carin.parser;

import carin.GameStates;
import carin.entities.GeneticEntity;
import carin.parser.ast.SyntaxError;
import carin.parser.ast.expressions.*;
import carin.parser.ast.statements.*;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GeneticParser {
    private GameStates states;
    private final GeneticEntity host;
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

    public Statement getProgram() throws SyntaxError {
        Statement program = parseProgram();
        return program;
    }

    /** this should be use for testing only
     * @param expr list of 1 expression
     * @return Expr AST
     * @throws SyntaxError
     */
    public Expr testExpr(List<String> expr) throws Exception {
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
        return " at " + path + ':' + tk.getInfo();
    }

    // Program → Statement+
    private Statement parseProgram() throws SyntaxError {
        StatementSeq seq = new StatementSeq();
        while (Token.isStatement(tk.peek())) {
            seq.add(parseStatement());
        }
        if (seq.size() < 1) throw new SyntaxError("at least 1 statement needed", errInfo());
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
            // parseBlock
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
            // parseWhile
            tk.consume();
            Expr condition = parseCondition();
            Statement todo = parseStatement();
            p = new WhileStatement(condition, todo, var_map);
        }
        //else throw new SyntaxError("something wrong", errInfo());
        return p;
    }

    // IfStatement → if ( Expression ) then Statement else Statement
    private Statement parseIf() throws SyntaxError {
        tk.consume(); // consume if
        Expr condition = parseCondition();
        Statement then;
        Statement otherwise;
        if (tk.peek("then")) {
            tk.consume();
            then = parseStatement();
        }
        else throw new SyntaxError("\"then\" expected", errInfo());
        if (tk.peek("else")) {
            tk.consume();
            otherwise = parseStatement();
        }
        else throw new SyntaxError("\"else\" expected", errInfo());
        return new IfStatement(condition, then, otherwise, var_map);
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
                cmd = new Assignment(var_map, var, v);
            }
            else throw new SyntaxError("expression expected", errInfo());
        }
        else if (Token.isAction(tk.peek())) {
            Token token = tk.consume();
            // parse Direction
            if (Token.isDirection(tk.peek())) {
                String direction = tk.consume().val();
                if (Token.isAttack(token)) cmd = new Action(states, host,'a');
                else cmd = new Action(states, host,'m');
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
        if (Token.isNUM(tk.peek()))
            throw new SyntaxError("not a statement", errInfo());
        else if (Token.isIDENTIFIER(tk.peek()))
            return v;
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
        else if (tk.consume("(")) {
            v = parseExpr();
            /*
            if (tk.peek().type() != Token.Type.DELIMITER)
                throw new SyntaxError("not a statement", errInfo());
            */
            if (!tk.consume(")"))
                throw new SyntaxError("\")\" expected", errInfo());
        }
        else throw new SyntaxError("something wrong", errInfo());
        return v;
    }
}
