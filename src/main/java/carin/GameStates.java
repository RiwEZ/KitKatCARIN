package carin;

import carin.entities.Antibody;
import carin.entities.GeneticEntity;
import carin.entities.Player;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.MapArea;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;

import java.util.ArrayList;
import java.util.Collection;


public final class GameStates {
    private static int virusCurrentID;
    private static int antibodyCurrentID;

    private static final ArrayList<GeneticEntity> entities = new ArrayList<>();
    private static LogicLoop loop;

    private GameStates() {
    }

    public static LogicLoop getLogicLoop() {
        return loop;
    }

    public static void init() {
        // init LogicLoop and run it
        if (loop == null) loop = new LogicLoop();
        loop.start();

        // Setup camera
        Camera camera = new FreeFlightCamera();
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        // Get all spawn points from Map
        Collection<Spawnpoint> allSpawn = Game.world().getEnvironment("map2").getSpawnpoints();
        Collection<MapArea> allArea = Game.world().getEnvironment("map2").getAreas();
        Game.world().onLoaded(e -> {
            Player p = Player.instance();
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = Game.random().choose(allSpawn);
                Virus virus = new Virus();
                entities.add(virus);
                spawn.spawn(virus);
            }
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = Game.random().choose(allSpawn);
                Antibody antibody = new Antibody();
                p.addAntibody(antibody);
                entities.add(antibody);
                spawn.spawn(antibody);
                }
        });
    }

    public static ArrayList<GeneticEntity> getEntities() {
        return entities;
    }

    public static Collection<MapArea> getMapArea() {
        return Game.world().getEnvironment("map2").getAreas();
    }

}
