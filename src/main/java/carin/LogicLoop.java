package carin;

import carin.entities.IGeneticEntity;
import carin.gui.IngameScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.util.TimeUtilities;

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
    public static boolean isPlayerWin = false;

    public LogicLoop() {}

    // should have mode like 1 -> normal, 2 -> x2 speed, 3 -> x3 speed
    public void setXSpeed(int multiplier) {
        delay = defaultDelay / multiplier;
    }

    public void setPause(boolean value) {
        isPause = value;
        IngameScreen.update(isPause);
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
        while (!interrupted() && !isGameOver) {
            final long start = System.nanoTime();
            if (!isPause) {
                tick++;
                process();
            }
            final double processTime = TimeUtilities.nanoToMs(System.nanoTime() - start);
            try {
                //System.out.println(processTime);
                delay(Math.round(processTime));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(isGameOver){
            Game.loop().perform(1000, () -> {
                Game.window().getRenderComponent().fadeIn(1000);
                Game.screens().display("GAMEOVER");
            });
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
            isPlayerWin = states.getAntibodyCount() > states.getVirusCount();
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
