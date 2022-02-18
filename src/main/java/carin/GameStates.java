package carin;

import carin.entities.*;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class contains all entities states and utility function to manipulate states
 */
public final class GameStates {
    // Min, Max location of Map
    public static final int minX = 36, maxX = 288;
    public static final int maxY = 36, minY = 288;

    private final ArrayList<GeneticEntity> entities = new ArrayList<>();
    private LogicLoop logic;

    private final Map<Point2D, GeneticEntity> entityMap = new ConcurrentHashMap<>();
    private final GeneticEntity unoccupied = new UnOccupied();

    private static final GameStates states = new GameStates();

    public static GameStates states() {
        return states;
    }

    private void spawnGeneticEntity(Spawnpoint point, GeneticEntity entity) {
        entities.add(entity);
        point.spawn((IEntity) entity);
        entityMap.put(point.getLocation(), entity);
    }

    public void init() {
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

    public class SensorIterator implements Iterator<GeneticEntity> {
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
        public GeneticEntity next() {
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

    public ArrayList<GeneticEntity> entities() {
        return entities;
    }

    public LogicLoop logicLoop() {
        return logic;
    }

    public Map<Point2D, GeneticEntity> entityMap() {
        return entityMap;
    }

    public boolean isUnOccupied(Point2D pos) {
        return entityMap.get(pos).equals(unoccupied);
    }

    public GeneticEntity unOccupied() {
        return unoccupied;
    }
}
