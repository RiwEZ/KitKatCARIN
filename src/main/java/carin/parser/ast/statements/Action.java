package carin.parser.ast.statements;

import carin.entities.IGeneticEntity;

import java.util.Map;

public class Action implements Statement {
    private final Character action; // a = attack, m = move
    private IGeneticEntity host;
    private final int direction;

    public Action(Character action, int direction) {
        this.action = action;
        this.direction = direction;
    }

    /**
     * @param x x position from host
     * @param y y position from host
     */
    private void makeActionAt(int x, int y) {
        if (action == 'a') {
            host.attack(x, y);
        }
        else if (action == 'm') {
            host.move(x, y);
        }
    }

    @Override
    public boolean evaluate(Map<String, Integer> var_map, IGeneticEntity host) {
        // direction is numpad number's relative position to number 5
        if (host == null || this.host != host) this.host = host;
        switch (direction) {
            case 1 -> makeActionAt(-1, 1);
            case 2 -> makeActionAt(0, 1);
            case 3 -> makeActionAt(1, 1);
            case 4 -> makeActionAt(-1, 0);
            case 6 -> makeActionAt(1, 0);
            case 7 -> makeActionAt(-1, -1);
            case 8 -> makeActionAt(0, -1);
            case 9 -> makeActionAt(1, -1);
        }
        return true;
    }
}
