package carin;

import carin.entities.GeneticEntity;
import de.gurkenlabs.litiengine.Game;

public class LogicLoop extends Thread {
    private long delay = 500;
    private boolean isPause = false;

    public LogicLoop() {}

    // should have mode like 1 -> normal, 2 -> x2 speed, 3 -> x3 speed
    public void setSpeed(int mode) {
        if (delay == 500) delay = 1000;
        else delay = 500;
    }

    public void togglePause() {
        isPause = !isPause;
    }

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

    private void runGeneticCode() {
        if (!Game.isInNoGUIMode()) {
            for (GeneticEntity g : GameStates.getEntities()) {
                g.run();
            }
        }
    }

    private void delay() throws InterruptedException {
        sleep(delay);
    }
}
