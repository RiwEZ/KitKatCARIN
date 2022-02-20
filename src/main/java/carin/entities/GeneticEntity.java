package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;

import java.awt.geom.Point2D;


@EntityInfo(width = 36, height = 36)
@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = true)
public abstract class GeneticEntity extends Creature implements IGeneticEntity {
    private final int maxHP;
    private final int damage;
    private final int leech;
    private int currentHP;
    protected final GameStates states = GameStates.states();
    private GeneticProgram geneticCode;
    private final String type;
    private final String name;

    public GeneticEntity(String type, String name) {
        super();
        this.type = type;
        this.name = name;
        if (type.equals("virus")) {
            this.maxHP = Config.virus_hp;
            this.damage = Config.virus_dmg;
            this.leech = Config.virus_leech;
            this.setSpritesheetName(type);
        }
        else if (type.equals("antibody")) {
            this.maxHP = Config.antibody_hp;
            this.damage = Config.antibody_dmg;
            this.leech = Config.antibody_leech;
            this.setSpritesheetName(type);
        }
        else {
            this.maxHP = 0;
            this.damage = 0;
            this.leech = 0;
            this.die();
        }

        this.currentHP = maxHP;
        addEntityRenderListener(e -> new StatusBar(this).render(e.getGraphics()));

        this.onDeath(e -> {
            states.addToRemove(this);
            e.setVisible(false);
        });
    }

    @Override
    public void setGeneticCode(GeneticProgram p) {
        this.geneticCode = p;
    }

    @Override
    public void move(double x, double y) {
        Point2D pos = new Point2D.Double(this.getX()+(x*36), this.getY()+(y*36));
        if (states.isInMap(pos)) {
            Point2D prevPos = new Point2D.Double(this.getX(), this.getY());
            if (states.isUnOccupied(pos)) {
                states.entityMap().put(pos, this);
                states.entityMap().put(prevPos, states.unOccupied());
                this.setLocation(pos);
            }
        }
    }

    public void manualMove(Point2D loc) {
        Point2D prevPos = new Point2D.Double(this.getX(), this.getY());
        if (states.isUnOccupied(GameStates.getSnapPoint(loc).getLocation())) {
            states.entityMap().put(GameStates.getSnapPoint(loc).getLocation(), this);
            states.entityMap().put(prevPos, states.unOccupied());
            this.setLocation(GameStates.getSnapPoint(loc).getLocation());
        }
    }

    @Override
    public void run() {
        geneticCode.run();
    }

    @Override
    public boolean attack(double x, double y) {
        Point2D pos = new Point2D.Double(this.getX()+(x*36), this.getY()+(y*36));
        if (states.isInMap(pos)) {
            if (!states.isUnOccupied(pos)) {
                IGeneticEntity target = states.entityMap().get(pos);
                target.getAttacked(this, damage);
                currentHP = Math.min(currentHP + leech, maxHP);
                return target.getCurrHP() == 0;
            }
        }
        return false;
    }

    @Override
    public void getAttacked(IGeneticEntity entity, int dmg) {
        if (this.isDead()) return;
        currentHP = Math.max(currentHP - dmg, 0);
        if (currentHP == 0) {
            this.die();
        }
    }

    @Override
    public int getCurrHP() {
        return currentHP;
    }

    @Override
    public IGeneticEntity getCopy() {
        IGeneticEntity g = states.unOccupied();
        if (type.equals("virus")) {
            g = new Virus(name);
            g.setGeneticCode(geneticCode.getCopy(g));
        }
        else if (type.equals("antibody")) {
            g = new Antibody(name);
            g.setGeneticCode(geneticCode.getCopy(g));
        }
        return g;
    }
}
