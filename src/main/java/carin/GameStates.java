package carin;

import carin.entities.*;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GameStates {
    // Min, Max location of Map
    public static final int minX = 36, maxX = 288;
    public static final int maxY = 36, minY = 288;

    private static final ArrayList<GeneticEntity> entities = new ArrayList<>();
    private static LogicLoop logic;

    private static final Map<Point2D, GeneticEntity> entityMap = new ConcurrentHashMap<>();
    private static final GeneticEntity unoccupied = new UnOccupied();

    private static void spawnGeneticEntity(Spawnpoint point, GeneticEntity entity) {
        entities.add(entity);
        point.spawn((IEntity) entity);
        entityMap.put(point.getLocation(), entity);
    }

    public static void init() {
        // init LogicLoop and run it
        if (logic == null) logic = new LogicLoop();
        logic.start();

        // Setup camera
        Camera camera = new FreeFlightCamera();
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        Game.world().onLoaded(env -> {
            Collection<Spawnpoint> allSpawn = env.getSpawnpoints();

            for (Spawnpoint point : allSpawn) {
                entityMap.put(point.getLocation(), unoccupied);
            }

            Player p = Player.instance();
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = new Spawnpoint();
                spawn.setLocation(Game.random().choose(allSpawn).getLocation());
                spawnGeneticEntity(spawn, new Virus());
            }
            // test spawn
            for(int i = 0; i < 3; i++){
                Spawnpoint spawn = new Spawnpoint();
                spawn.setLocation(Game.random().choose(allSpawn).getLocation());
                spawnGeneticEntity(spawn, new Antibody());
            }
        });

        Game.world().loadEnvironment("map2");
    }

    public static ArrayList<GeneticEntity> getEntities() {
        return entities;
    }

    public static LogicLoop getLogicLoop() {
        return logic;
    }

    public static Map<Point2D, GeneticEntity> getEntityMap() {
        return entityMap;
    }

    public static boolean isUnOccupied(Point2D pos) {
        return entityMap.get(pos).equals(unoccupied);
    }

    public static GeneticEntity unOccupied() {
        return unoccupied;
    }
}
