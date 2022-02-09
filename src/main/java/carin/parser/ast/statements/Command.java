package carin.parser.ast.statements;

public class Command implements Statement {
    Statement statement;

    public Command(Action statement) {
        this.statement = statement;
    }

    public Command(Assignment statement) {
        this.statement = statement;
    }

    @Override
    public void evaluate() {
        if (statement != null) statement.evaluate();
    }
}
