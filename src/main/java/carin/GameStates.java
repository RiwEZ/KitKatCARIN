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

    // Min, Max location of Map
    public static final int minX = 36, maxX = 288;
    public static final int maxY = 36, minY = 288;

    // Virus and Antibody list
    private static final ArrayList<Antibody> antibodyList = new ArrayList<>();
    private static final ArrayList<Virus> virusList = new ArrayList<>();

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

        Game.world().onLoaded(e -> {
            Player p = Player.instance();
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = new Spawnpoint();
                spawn.setLocation(Game.random().choose(allSpawn).getLocation());
                Virus virus = new Virus();
                entities.add(virus);
                virusList.add(virus);
                spawn.spawn(virus);
            }
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = new Spawnpoint();
                spawn.setLocation(Game.random().choose(allSpawn).getLocation());
                Antibody antibody = new Antibody();
                p.addAntibody(antibody);
                entities.add(antibody);
                antibodyList.add(antibody);
                spawn.spawn(antibody);
                }
        });
    }

    public static ArrayList<GeneticEntity> getEntities() {
        return entities;
    }

    public static ArrayList<Antibody> getListAnti() {
        return antibodyList;
    }

    public static ArrayList<Virus> getListVirus() {
        return virusList;
    }

}
