package carin;

import carin.entities.*;
import carin.util.*;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.EnvironmentLoadedListener;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.util.TimeUtilities;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class contains all entities states and utility function to manipulate states
 */
public class GameStates {
    private static final GameStates states = new GameStates();

    private final List<IGeneticEntity> entities = new LinkedList<>();
    private final Map<Point2D, IGeneticEntity> entityMap = new ConcurrentHashMap<>();
    private int antibodyCount = 0;
    private int virusCount = 0;

    private final List<IGeneticEntity> toRemove = new LinkedList<>();
    private final List<IGeneticEntity> toSpawn = new ArrayList<>();

    private final IGeneticEntity unoccupied = new UnOccupied();
    private final Spawnpoint spawnpoint = new Spawnpoint();

    private boolean initialized = false;
    private double initTime;
    private List<Antibody> availableAntibody;
    private List<Virus> availableVirus;

    private InputController inputController;

    private static LogicLoop loop;

    public static GameStates states() {
        return states;
    }

    private void defaultSpawn() {
        // virus spawn
        for(int i = 0; i < 3; i++){
            Point2D pos = Game.random().choose(unoccupiedPos());
            spawnGeneticEntity(pos, Game.random().choose(availableVirus).getCopy());
        }
        // antibody spawn
        for(int i = 0; i < 3; i++){
            Point2D pos = Game.random().choose(unoccupiedPos());
            spawnGeneticEntity(pos, availableAntibody.get(0).getCopy());
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

        Game.world().loadEnvironment(map3x3);
    }

    public void init() {
        final long start = System.nanoTime();

        if (initialized) {
            entities.clear();
            entityMap.clear();
            antibodyCount = 0;
            virusCount = 0;
            toSpawn.clear();
            toRemove.clear();
            inputController.clearListener();
            Game.world().unloadEnvironment();
        }

        // Setup camera
        CameraManager.getCamera().setClampToMap(false);

        EnvironmentLoadedListener onLoad = env -> {
            CameraManager.defaultCamSetup();
            CameraManager.camFunction();
            defaultSpawn();
        };

        Game.world().onLoaded(onLoad);
        Game.world().onUnloaded(e -> {
            Game.world().removeLoadedListener(onLoad);
            e.clear();
        });

        inputController = new InputController();
        inputController.initListener();

        // generate map and load it to the game
        IMap MAP = MapGeneration.generateMap(Config.map_m, Config.map_n);

        // init Player
        Player.instance().setCredit(Config.initial_credits);

        initialized = true;
        availableAntibody = GeneticEntityFactory.getAvailableAntibody();
        availableVirus = GeneticEntityFactory.getAvailableVirus();
        Game.world().loadEnvironment(MAP); // load huge number of tile is slow

        // init LogicLoop and run it
        loop = new LogicLoop();
        loop.start();

        initTime = TimeUtilities.nanoToMs(System.nanoTime() - start);
    }

    public static LogicLoop loop() {
        return loop;
    }

    public boolean isInit() {
        return initialized;
    }

    public double initTime() {
        return initTime;
    }

    public void spawnGeneticEntity(Point2D pos, IGeneticEntity entity) {
        spawnpoint.setLocation(pos);
        if (entityMap.get(pos).equals(unoccupied)) {
            SoundManager.spawnSound();
            entities.add(entity);
            entityMap.put(pos, entity);
            if (entity.getClass() == Antibody.class) antibodyCount++;
            if (entity.getClass() == Virus.class) virusCount++;
        }
        spawnpoint.spawn(entity);
    }

    public SensorIterator sensorIter(Point2D host) {
        return new SensorIterator(host, this, Config.tile_width, Config.tile_height);
    }

    public boolean isInMap(Point2D pos) {
        return entityMap.get(pos) != null;
    }

    public List<IGeneticEntity> entities() {
        return entities;
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
        Game.window().cursor().set(Program.Cursor);
        return entityMap.get(pos).equals(unoccupied);
    }

    public IGeneticEntity unOccupied() {
        return unoccupied;
    }

    public Set<Point2D> unoccupiedPos() {
        Set<Point2D> empty = new HashSet<>();
        for (Map.Entry<Point2D, IGeneticEntity> m : entityMap().entrySet()) {
            if (m.getValue().equals(unoccupied))
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

    public List<IGeneticEntity> getToSpawn() {
        return toSpawn;
    }

    public void triggerToSpawn() {
        for (IGeneticEntity entity : toSpawn) {
            spawnGeneticEntity(entity.getLocation(), entity);
        }
        toSpawn.clear();
    }

    public Point2D getSnap(Point2D mouse) {
        double x = Config.tile_width * Math.floor(mouse.getX()/Config.tile_width);
        double y = Config.tile_height * Math.floor(mouse.getY()/Config.tile_height);
        return new Point2D.Double(x, y);
    }

    public Antibody getFocusedAntibody(Point2D snap) {
        if(Game.world().getEnvironments() == null){
            return null;
        }
        if (entityMap.get(snap) != null && entityMap.get(snap).getClass() == Antibody.class) {
            return (Antibody) entityMap.get(snap);
        }
        return null;
    }

    public List<Antibody> getAvailableAntibody() {
        return Collections.unmodifiableList(availableAntibody);
    }
}
