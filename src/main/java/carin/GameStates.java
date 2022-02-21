package carin;

import carin.entities.*;
import carin.util.CameraManager;
import carin.util.MapGeneration;
import carin.util.SensorIterator;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class contains all entities states and utility function to manipulate states
 */
public final class GameStates {
    private static final GameStates states = new GameStates();

    // Generate MAP
    private final IMap MAP = MapGeneration.generateMap(Config.map_m, Config.map_n);

    private final ArrayList<IGeneticEntity> entities = new ArrayList<>();
    private final Map<Point2D, IGeneticEntity> entityMap = new ConcurrentHashMap<>();
    private int antibodyCount = 0;
    private int virusCount = 0;

    private final ArrayList<IGeneticEntity> toRemove = new ArrayList<>();
    private final ArrayList<IGeneticEntity> toSpawn = new ArrayList<>();

    private LogicLoop logic;

    private final IGeneticEntity unoccupied = new UnOccupied();
    private final Spawnpoint spawnpoint = new Spawnpoint();
    private final Player player = Player.instance();

    private boolean initialized = false;
    private ArrayList<Antibody> availableAntibody;
    private ArrayList<Virus> availableVirus;

    private static Antibody currentFocus;

    public static GameStates states() {
        return states;
    }

    private Point2D mouseManual;

    private void defaultSpawn() {
        // virus spawn
        for(int i = 0; i < 3; i++){
            Point2D pos = Game.random().choose(unoccupiedPos());
            spawnGeneticEntity(pos, Game.random().choose(availableVirus).getCopy());
        }
        // antibody spawn
        for(int i = 0; i < 3; i++){
            Point2D pos = Game.random().choose(unoccupiedPos());
            spawnGeneticEntity(pos, Game.random().choose(availableAntibody).getCopy());
        }
    }

    /**
     * this should be use for testing only
     */
    public void initTest(int m, int n) {
        if (Game.hasStarted()) Game.exit();
        IMap map3x3 = MapGeneration.generateMap(m, n);

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
        initialized = true;

        availableAntibody = GeneticEntityFactory.getAvailableAntibody();
        availableVirus = GeneticEntityFactory.getAvailableVirus();

        // Setup camera
        CameraManager.getCamera().setClampToMap(false);

        Game.world().onLoaded(env -> {
            CameraManager.defaultCamSetup();
            CameraManager.camFunction();
            Collection<Spawnpoint> allSpawn = env.getSpawnpoints();
            for (Spawnpoint point : allSpawn) {
                entityMap.put(point.getLocation(), unoccupied);
            }
            defaultSpawn();
        });

        // Manual move antibody by pressing and drag
        Input.mouse().onPressed((e) -> {
            setCurrentFocus(getFocusedAntibody());
        });
        Input.mouse().onDragged(e -> {
            mouseManual = Input.mouse().getMapLocation();
        });
        Input.mouse().onReleased(k -> {
            if(getCurrentFocus() != null){
                getCurrentFocus().manualMove(mouseManual);
            }
        });


        Game.world().loadEnvironment(MAP);

        // init LogicLoop and run it
        if (logic == null) logic = new LogicLoop();
        logic.start();
    }

    public boolean isInit() {
        return initialized;
    }

    public void spawnGeneticEntity(Point2D pos, IGeneticEntity entity) {
        spawnpoint.setLocation(pos);
        if (entityMap.get(pos).equals(unoccupied)) {
            entities.add(entity);
            entityMap.put(pos, entity);
            if (entity.getClass() == Antibody.class) antibodyCount++;
            if (entity.getClass() == Virus.class) virusCount++;
        }
        spawnpoint.spawn(entity);
    }

    public SensorIterator sensorIter(Point2D host) {
        return new SensorIterator(host, this);
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

    public int getAntibodyCount() {
        return antibodyCount;
    }

    public int getVirusCount() {
        return virusCount;
    }

    public boolean isUnOccupied(Point2D pos) {
        return entityMap.get(pos).equals(unoccupied);
    }

    public IGeneticEntity unOccupied() {
        return unoccupied;
    }

    public Set<Point2D> unoccupiedPos() {
        Set<Point2D> empty = new HashSet<>();
        for (Map.Entry<Point2D, IGeneticEntity> m : states.entityMap().entrySet()) {
            if (m.getValue().equals(states.unOccupied()))
                empty.add(m.getKey());
        }
        return empty;
    }

    public Virus randomVirus() {
        return (Virus) Game.random().choose(availableVirus).getCopy();
    }

    public void addToRemove(IGeneticEntity entity) {
        toRemove.add(entity);
    }

    public void clearToRemove() {
        for (IGeneticEntity entity : toRemove) {
            entityMap.put(entity.getLocation(), states.unOccupied());
            entities.remove(entity); // this will slow things down
            if (entity.getClass() == Antibody.class) antibodyCount--;
            if (entity.getClass() == Virus.class) virusCount--;
        }
        toRemove.clear();
    }

    public void addToSpawn(IGeneticEntity entity) {
        toSpawn.add(entity);
    }

    public ArrayList<IGeneticEntity> getToSpawn() {
        return toSpawn;
    }

    public void triggerToSpawn() {
        for (IGeneticEntity entity : toSpawn) {
            spawnGeneticEntity(entity.getLocation(), entity);
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
        Optional<Spawnpoint> point = points.stream().filter(x -> x.getBoundingBox().contains(mouse)).findAny();
        return point.orElse(null);
    }

    private static Antibody getFocusedAntibody() {
        if(Game.world().getEnvironments() == null){
            return null;
        }
        Collection<Antibody> antibodies = Game.world().environment().getEntities(Antibody.class);
        Optional<Antibody> selectAntibody = antibodies.stream().filter(x -> x.getCollisionBox().contains(Input.mouse().getMapLocation())).findFirst();
        return selectAntibody.orElse(null);
    }
}
