package carin;

import carin.entities.Antibody;
import carin.entities.Player;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.MapArea;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


public final class GameStates {
    private static int virusCurrentID;
    private static int antibodyCurrentID;

    private static ArrayList<Antibody> antibodyList = new ArrayList<>();
    private static ArrayList<Virus> virusList = new ArrayList<>();

    private static final Random rand = new Random();

    private GameStates() {
    }

    public static void init() {
        // Setup camera
        Camera camera = new FreeFlightCamera();
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        // Get all spawn points from Map
        Collection<Spawnpoint> allSpawn = Game.world().getEnvironment("map2").getSpawnpoints();
        Collection<MapArea> allArea = Game.world().getEnvironment("map2").getAreas();
        Game.world().onLoaded(e -> {
            Player p = Player.instance();
            antibodyList = p.getAntibodyList();
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = Game.random().choose(allSpawn);
                Virus virus = new Virus();
                virusList.add(virus);
                spawn.spawn(virus);
            }
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = Game.random().choose(allSpawn);
                Antibody antibody = new Antibody();
                p.addAntibody(antibody);
                antibodyList.add(antibody);
                spawn.spawn(antibody);
                }
        });
    }

    public static Collection<MapArea> getMapArea() {
        return Game.world().getEnvironment("map2").getAreas();
    }


}
