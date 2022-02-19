package carin;

import carin.entities.IGeneticEntity;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class LogicLoop extends Thread {
    private long delay = 500;
    private boolean isPause = true;
    private final float spawn_rate = Config.spawn_rate;
    private final Random rand = new Random();
    private final GameStates states = GameStates.states();

    public LogicLoop() {}

    // should have mode like 1 -> normal, 2 -> x2 speed, 3 -> x3 speed
    public void setSpeed(int mode) {
        if (delay == 500) delay = 1000;
        else delay = 500;
    }

    public void togglePause() {
        isPause = !isPause;
    }

    public boolean isPause() { return isPause; }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                if (!isPause) {
                    runGeneticCode();
                    delay();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void spawnVirus() {
        if (rand.nextFloat() <= spawn_rate) {
            Set<Point2D> empty = new HashSet<>();
            for (Map.Entry<Point2D, IGeneticEntity> m : states.entityMap().entrySet()) {
                if (m.getValue().equals(states.unOccupied()))
                    empty.add(m.getKey());
            }
            Point2D p = Game.random().choose(empty);
            if (p != null) states.spawnGeneticEntity(new Spawnpoint(p), new Virus());
        }
    }

    private void runGeneticCode() {
        spawnVirus();
        for (IGeneticEntity g : states.entities()) {
            g.run();
        }
        states.clearToRemove();
        states.triggerToSpawn();
    }

    private void delay() throws InterruptedException {
        sleep(delay);
    }
}
