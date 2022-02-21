package carin;

import carin.entities.*;
import carin.util.MapGeneration;
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

    private static final GameStates states = new GameStates();

    public static GameStates states() {
        return states;
    }

    // focus on The antibody
    private static Antibody currentFocus;
    private Point2D mouseManual;

    // Camera things
    private static Camera camera;
    private static float zoomAmount;
    private static float MaxZoomOut;
    private static float defaultFocusX, defaultFocusY;
    private static float currentFocusX, currentFocusY;
    private static float mouseX, mouseY;

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
        camera = new Camera();
        camera.setClampToMap(false);

        Game.world().onLoaded(env -> {
            defaultCamSetup();
            camFunction();
            Collection<Spawnpoint> allSpawn = env.getSpawnpoints();
            for (Spawnpoint point : allSpawn) {
                entityMap.put(point.getLocation(), unoccupied);
            }
            defaultSpawn();
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
            return lookAt == 10 || lookingPos() != null;
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

    // Setup camera
    private static void defaultCamSetup() {
        MaxZoomOut = (float) ((float) 1 / ((Game.world().environment().getCenter().getX() + Game.world().environment().getCenter().getY()) * 0.005));
        zoomAmount = MaxZoomOut;
        defaultFocusX = (float) (Game.world().environment().getCenter().getX() - Game.world().environment().getCenter().getX());
        defaultFocusY = (float) Game.world().environment().getCenter().getY();
        currentFocusX = defaultFocusX;
        currentFocusY = defaultFocusY;
        camera.setFocus(currentFocusX, currentFocusY);
        camera.setZoom(zoomAmount, 0);
        Game.world().setCamera(camera);
    }

    // still buggy...
    private static void camFunction() {
        // Zoom-in / Zoom-out by MouseWheel
        Input.mouse().onWheelMoved(x -> {
            if(Input.keyboard().isPressed(17)){
                if(x.getPreciseWheelRotation() == -1){
                    zoomAmount += x.getScrollAmount() * 0.0125;
                }
                else{
                    zoomAmount -= x.getScrollAmount() * 0.0125;
                    if(zoomAmount <= MaxZoomOut){
                        zoomAmount = MaxZoomOut;
                        camera.setFocus(defaultFocusX, defaultFocusY);
                    }
                }
                camera.setZoom(zoomAmount, 0);
            }
        });
        // Pan Camera by Dragging
        Input.mouse().onPressed(k -> {
            mouseX = k.getXOnScreen();
            mouseY = k.getYOnScreen();
        });
        Input.mouse().onDragged(x -> {
            if(Input.keyboard().isPressed(17)){
                if (zoomAmount > MaxZoomOut + 0.1) {
                    if(Math.abs((x.getXOnScreen() - mouseX)) > Math.abs((x.getYOnScreen() - mouseY))){
                        if(x.getXOnScreen() > mouseX){
                            currentFocusX -= 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                        if (x.getXOnScreen() < mouseX){
                            currentFocusX += 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                    }
                    if (Math.abs((x.getYOnScreen() - mouseY)) > Math.abs((x.getXOnScreen() - mouseX))){
                        if(x.getYOnScreen() > mouseY){
                            currentFocusY -= 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                        if (x.getYOnScreen() < mouseY){
                            currentFocusY += 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                    }
                }
            }
        });
    }

    public static void dieScreenShake() {
        camera.shake(2,0, 300);
    }

}
