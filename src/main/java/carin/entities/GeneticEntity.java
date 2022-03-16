package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import carin.parser.GeneticProgram;
import carin.util.CameraManager;
import carin.util.SoundManager;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityRenderListener;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.geom.Point2D;
import java.util.Collection;

public abstract class GeneticEntity extends Creature implements IGeneticEntity {
    protected final GameStates states = GameStates.states();
    private final int maxHP;
    private final int damage;
    private final int leech;
    private final String type;
    private final String name;
    private int currentHP;
    private GeneticProgram geneticCode;

    public GeneticEntity(String type, String name) {
        super();
        this.type = type;
        this.name = name;
        if (type.equals("virus")) {
            this.maxHP = Config.virus_hp;
            this.damage = Config.virus_dmg;
            this.leech = Config.virus_leech;
        } else if (type.equals("antibody")) {
            this.maxHP = Config.antibody_hp;
            this.damage = Config.antibody_dmg;
            this.leech = Config.antibody_leech;
        } else {
            this.maxHP = 0;
            this.damage = 0;
            this.leech = 0;
            this.die();
        }

        this.setRenderType(RenderType.GROUND);

        this.currentHP = maxHP;
        this.setScaling(true);
        this.setSize(Config.tile_width, Config.tile_height);
        EntityRenderListener status = e -> new StatusBar(this).render(e.getGraphics());
        this.addEntityRenderListener(status);

        this.onDeath(e -> {
            states.addToRemove(this);
            e.setVisible(false);
            this.removeListener(status);
        });
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setGeneticCode(GeneticProgram p) {
        this.geneticCode = p;
    }

    private boolean containSprite(String name) {
        Collection<Spritesheet> spriteSheets = Resources.spritesheets().getAll();
        for (Spritesheet ss : spriteSheets) {
            if (ss.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    protected void setSprite(String defaultSprite) {
        if (!containSprite(name))
            this.setSpritesheetName(defaultSprite);
        else
            this.setSpritesheetName(name);
    }

    protected void setLoc(Point2D prevPos, Point2D pos) {
        states.entityMap().put(pos, this);
        states.entityMap().put(prevPos, states.unOccupied());
        this.setLocation(pos);
    }

    @Override
    public void move(double x, double y) {
        if (!this.isDead()) {
            Point2D pos = new Point2D.Double(this.getX() + (x * Config.tile_width), this.getY() + (y * Config.tile_height));
            if (states.isInMap(pos)) {
                Point2D prevPos = new Point2D.Double(this.getX(), this.getY());
                if (states.isUnOccupied(pos)) {
                    boolean empty = true;
                    for (IGeneticEntity entity : states.getToSpawn()) {
                        if (entity.getLocation().equals(pos)) {
                            empty = false;
                            break;
                        }
                    }
                    if (empty) {
                        SoundManager.moveSound();
                        setLoc(prevPos, pos);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        geneticCode.run();
    }

    @Override
    public boolean attack(double x, double y) {
        if (!this.isDead()) {
            Point2D pos = new Point2D.Double(this.getX() + (x * Config.tile_width), this.getY() + (y * Config.tile_height));
            if (states.isInMap(pos) && !states.isUnOccupied(pos)) {
                SoundManager.attackSound();
                IGeneticEntity target = states.entityMap().get(pos);
                if (target.getAttacked(this, damage))
                    if(this.type.equals("virus")) currentHP = Math.min(currentHP + leech, maxHP);
                    else if(this.type.equals("antibody") && target.getCurrHP() == 0) currentHP = Math.min(currentHP + leech, maxHP);
                return target.getCurrHP() == 0;
            }
        }
        return false;
    }

    @Override
    public boolean getAttacked(IGeneticEntity entity, int dmg) {
        if (this.isDead()) {
            currentHP = 0;
            //states.addToRemove(this);
            return false;
        }

        currentHP = Math.max(currentHP - dmg, 0);
        if (currentHP == 0) {
            SoundManager.lastHitSound();
            CameraManager.dieScreenShake();
            SoundManager.knockSound();
            this.die();
        }
        return true;
    }

    @Override
    public int getCurrHP() {
        return currentHP;
    }

    @Override
    public void setCurrHP(int hp) {
        this.currentHP = Math.max(hp, 0);
    }

    @Override
    public IGeneticEntity getCopy() {
        IGeneticEntity g = states.unOccupied();
        if (type.equals("virus")) {
            g = new Virus(name);
            g.setGeneticCode(geneticCode.getCopy(g));
        } else if (type.equals("antibody")) {
            g = new Antibody(name);
            g.setGeneticCode(geneticCode.getCopy(g));
        }
        return g;
    }

    public int getMaxHP() {
        return maxHP;
    }
}
