package carin;

import carin.entities.*;
import carin.util.MapGeneration;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;
import de.gurkenlabs.litiengine.input.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class contains all entities states and utility function to manipulate states
 */
public final class GameStates {
    // Generate MAP
    private final IMap MAP = MapGeneration.generateMap(Config.map_m, Config.map_n);

    private final ArrayList<IGeneticEntity> entities = new ArrayList<>();
    private final ArrayList<IGeneticEntity> toRemove = new ArrayList<>();
    private final ArrayList<IGeneticEntity> toSpawn = new ArrayList<>();

    private LogicLoop logic;

    private final Map<Point2D, IGeneticEntity> entityMap = new ConcurrentHashMap<>();
    private final IGeneticEntity unoccupied = new UnOccupied();
    private final Player player = Player.instance();

    private static final GameStates states = new GameStates();

    public static GameStates states() {
        return states;
    }

    // focus on The antibody
    private static Antibody currentFocus;
    private Point2D mouseManual;

    public void defaultSpawn(Collection<Spawnpoint> allSpawn) {
        Spawnpoint spawn = new Spawnpoint();
        // test spawn
        for(int i = 0; i < 3; i++){
            spawn.setLocation(Game.random().choose(allSpawn).getLocation());
            spawnGeneticEntity(spawn, new Virus());
        }
        // test spawn
        for(int i = 0; i < 3; i++){
            spawn.setLocation(Game.random().choose(allSpawn).getLocation());
            Antibody antibody = new Antibody();
            spawnGeneticEntity(spawn, antibody);
        }
    }

    /**
     * this should be use for testing only
     */
    public void initTest() {
        if (Game.hasStarted()) Game.exit();
        IMap map3x3 = MapGeneration.generateMap(3, 3);

        Game.hideGUI(true);
        Game.init();
        Game.world().onLoaded(env -> {
            Collection<Spawnpoint> allSpawn = env.getSpawnpoints();
            for (Spawnpoint point : allSpawn) {
                entityMap.put(point.getLocation(), unoccupied);
            }
        });

        Game.world().loadEnvironment(map3x3);
    }


    public void init() {
        // Setup camera
        Camera camera = new FreeFlightCamera();
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        Game.world().onLoaded(env -> {
            Collection<Spawnpoint> allSpawn = env.getSpawnpoints();
            for (Spawnpoint point : allSpawn) {
                entityMap.put(point.getLocation(), unoccupied);
            }
            defaultSpawn(allSpawn);
        });


        // Manual move antibody by pressing and drag
        Input.mouse().onPressing(() -> {
            setCurrentFocus(getFocusedAntibody());
        });
        Input.mouse().onDragged(e -> {
            mouseManual = Input.mouse().getMapLocation();
            if(getCurrentFocus() != null){
                getCurrentFocus().manualMove(mouseManual);
            }
        });


        Game.world().loadEnvironment(MAP);

        // init LogicLoop and run it
        if (logic == null) logic = new LogicLoop();
        logic.start();
    }

    public void spawnGeneticEntity(Spawnpoint point, IGeneticEntity entity) {
        if (entityMap.get(point.getLocation()).equals(unoccupied)) {
            entities.add(entity);
            entityMap.put(point.getLocation(), entity);
        }
        point.spawn(entity);
    }

    public class SensorIterator implements Iterator<IGeneticEntity> {
        Point2D host;
        int lookAt;

        public SensorIterator(Point2D host) {
            this.host = host;
            this.lookAt = 10;
        }

        private Point2D pos(double x, double y) {
            return new Point2D.Double(host.getX() + 36*x, host.getY() + 36*y);
        }

        private Point2D lookingPos() {
            int m = lookAt / 10;
            int i = lookAt % 10;

            return switch (i) {
                case 1 -> pos(0, -1*m);
                case 2 -> pos(m, -1*m);
                case 3 -> pos(m, 0);
                case 4 -> pos(m, m);
                case 5 -> pos(0, m);
                case 6 -> pos(-1*m, m);
                case 7 -> pos(-1*m, 0);
                case 8 -> pos(-1*m, -1*m);
                default -> null;
            };
        }

        public int getLookAt() {
            return lookAt;
        }

        @Override
        public boolean hasNext() {
            return lookingPos() != null;
        }

        @Override
        public IGeneticEntity next() {
            if (!hasNext()) return null;
            lookAt++;
            Point2D p = lookingPos();

            while (lookAt != 0) {
                if ((lookAt % 10) > 8) {
                    lookAt = lookAt + 10 - 8;
                    p = lookingPos();
                }
                if (lookAt > 40) {
                    lookAt = 0;
                    return null;
                }

                if (isInMap(p) && !isUnOccupied(p)) break;
                lookAt++;
                p = lookingPos();
            }
            return entityMap.get(p);
        }
    }

    public SensorIterator sensorIter(Point2D host) {
        return new SensorIterator(host);
    }

    public boolean isInMap(Point2D pos) {
        return !(entityMap.get(pos) == null);
    }

    public ArrayList<IGeneticEntity> entities() {
        return entities;
    }

    public LogicLoop logicLoop() {
        return logic;
    }

    public Map<Point2D, IGeneticEntity> entityMap() {
        return entityMap;
    }

    public boolean isUnOccupied(Point2D pos) {
        return entityMap.get(pos).equals(unoccupied);
    }

    public IGeneticEntity unOccupied() {
        return unoccupied;
    }

    public void addToRemove(IGeneticEntity entity) {
        toRemove.add(entity);
    }

    public void clearToRemove() {
        for (IGeneticEntity entity : toRemove) {
            entityMap.put(entity.getLocation(), states.unOccupied());
            entities.remove(entity);
        }
        toRemove.clear();
    }

    public void addToSpawn(IGeneticEntity entity) {
        toSpawn.add(entity);
    }

    public void triggerToSpawn() {
        for (IGeneticEntity entity : toSpawn) {
            Spawnpoint point = new Spawnpoint();
            point.setLocation(entity.getLocation());
            spawnGeneticEntity(point, entity);
        }
        toSpawn.clear();
    }

    public Player player() {
        return player;
    }

    public static Antibody getCurrentFocus() {
        return currentFocus;
    }

    public static void setCurrentFocus(Antibody focus){
        currentFocus = focus;
    }

    public static Spawnpoint getSnapPoint(Point2D mouse) {
        Collection<Spawnpoint> points = Game.world().environment().getSpawnpoints();
        Optional<Spawnpoint> point = points.stream().filter(x -> x.getBoundingBox().contains(mouse)).findFirst();
        return point.orElse(null);
    }

    private static Antibody getFocusedAntibody() {
        if(Game.world().getEnvironments() == null){
            return null;
        }
        Collection<Antibody> antibodies = Game.world().environment().getEntities(Antibody.class);
        Optional<Antibody> selectAntibody = antibodies.stream().filter(x -> x.getHitBox().contains(Input.mouse().getMapLocation())).findAny();
        return selectAntibody.orElse(null);
    }
}
