package carin;

import carin.entities.IGeneticEntity;
import de.gurkenlabs.litiengine.Game;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GameStatesTest {

    @Test
    void restartCorrectly() {
        Game.hideGUI(true);
        Game.init();
        GameStates states = GameStates.states();

        states.init();
        List<IGeneticEntity> entities = states.entities();
        Map<Point2D, IGeneticEntity> entityMap = states.entityMap();
        int antibody = states.getAntibodyCount();
        int virus = states.getVirusCount();
        int tick = GameStates.loop().getTick();

        // let loop run for 2 sec
        GameStates.loop().togglePause();
        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GameStates.loop().togglePause();

        states.init();
        assertEquals(entities, states.entities());
        assertEquals(entityMap, states.entityMap());
        assertEquals(antibody, states.getAntibodyCount());
        assertEquals(virus, states.getVirusCount());
        assertEquals(tick, GameStates.loop().getTick());
    }

}