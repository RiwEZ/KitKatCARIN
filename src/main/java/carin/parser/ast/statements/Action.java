package carin.parser.ast.statements;

import carin.GameStates;
import carin.entities.GeneticEntity;

// TODO: make this work
public class Action implements Statement {
    private final Character action; // a = attack, m = move
    private final GeneticEntity host;
    private final GameStates states;

    public Action(GameStates states, GeneticEntity host, Character action) {
        this.states = states;
        this.action = action;
        this.host = host;
    }

    @Override
    public boolean evaluate() {
        if (action == 'a') {
        }
        else if (action == 'm') {

        }
        return true;
    }
}
