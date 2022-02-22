package carin;

import carin.entities.IGeneticEntity;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderEngine;
import de.gurkenlabs.litiengine.util.TimeUtilities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class LogicLoop extends Thread {
    private final long defaultDelay = 500;
    private long delay = defaultDelay;
    private boolean isPause = true;
    private boolean isGameOver = false;
    private final float spawn_rate = Config.spawn_rate;
    private final Random rand = new Random();
    private final GameStates states = GameStates.states();
    private int tick = 0;

    private static LogicLoop instance;

    private LogicLoop() {}

    public static LogicLoop instance() {
        if (instance == null) instance = new LogicLoop();
        return instance;
    }

    // should have mode like 1 -> normal, 2 -> x2 speed, 3 -> x3 speed
    public void setXSpeed(int multiplier) {
        delay = defaultDelay / multiplier;
    }

    public void togglePause() {
        isPause = !isPause;
    }

    public boolean isPause() { return isPause; }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getTick() {
        return tick;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            final long start = System.nanoTime();
            if (!isPause) {
                tick++;
                process();
            }
            final double processTime = TimeUtilities.nanoToMs(System.nanoTime() - start);
            try {
                delay(Math.round(processTime));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void spawnVirus() {
        if (rand.nextFloat() <= spawn_rate) {
            Point2D p = Game.random().choose(states.unoccupiedPos());
            if (p != null) states.spawnGeneticEntity(p, states.randomVirus());
        }
    }

    private void process() {
        if (states.getAntibodyCount() == 0 || states.getVirusCount() == 0) {
            isGameOver = true;
            isPause = true;
            return;
        }
        spawnVirus();
        for (IGeneticEntity g : states.entities()) {
            g.run();
        }
        states.clearToRemove();
        states.triggerToSpawn();
    }

    private void delay(long processTime) throws InterruptedException {
        sleep(Math.max(0, delay - processTime));
    }
}
