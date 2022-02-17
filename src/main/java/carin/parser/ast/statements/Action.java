package carin.parser.ast.statements;

import carin.GameStates;
import carin.entities.GeneticEntity;

import java.util.Map;


// TODO: make this work
public class Action implements Statement {
    private final Character action; // a = attack, m = move
    private GeneticEntity host;
    private final GameStates states;
    private final int direction;

    public Action(GameStates states, Character action, int direction) {
        this.states = states;
        this.action = action;
        this.direction = direction;
    }

    /**
     * @param x x position from host
     * @param y y position from host
     */
    private void makeActionAt(int x, int y) {
        if (action == 'a') {
            // calc position from host
            // use states to attack it?
        }
        else if (action == 'm') {
            // calc position from host
            // check states if there are anything in that position
            host.move(x, y);
        }
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, GeneticEntity host) {
        // direction is numpad number's relative position to number 5
        if (host == null || this.host != host) this.host = host;
        switch (direction) {
            case 1 -> makeActionAt(-1, -1);
            case 2 -> makeActionAt(0, -1);
            case 3 -> makeActionAt(1, -1);
            case 4 -> makeActionAt(-1, 0);
            case 6 -> makeActionAt(1, 0);
            case 7 -> makeActionAt(-1, 1);
            case 8 -> makeActionAt(0, 1);
            case 9 -> makeActionAt(1, 1);
        }
        return true;
    }
}
