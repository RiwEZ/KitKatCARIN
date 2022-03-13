package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import carin.parser.GeneticProgram;
import carin.util.CameraManager;
import carin.util.SoundManager;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.RenderType;

import java.awt.geom.Point2D;

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
            this.setSpritesheetName("virus1");
        }
        else if (type.equals("antibody")) {
            this.maxHP = Config.antibody_hp;
            this.damage = Config.antibody_dmg;
            this.leech = Config.antibody_leech;
            this.setSpritesheetName("antibody1");
        }
        else {
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
    public void setGeneticCode(GeneticProgram p) {
        this.geneticCode = p;
    }

    protected void setLoc(Point2D prevPos, Point2D pos) {
        states.entityMap().put(pos, this);
        states.entityMap().put(prevPos, states.unOccupied());
        this.setLocation(pos);
    }

    @Override
    public void move(double x, double y) {
        Point2D pos = new Point2D.Double(this.getX()+(x*Config.tile_width), this.getY()+(y*Config.tile_height));
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

    @Override
    public void run() {
        geneticCode.run();
    }

    @Override
    public boolean attack(double x, double y) {
        Point2D pos = new Point2D.Double(this.getX()+(x*Config.tile_width), this.getY()+(y*Config.tile_height));
        if (states.isInMap(pos)) {
            if (!states.isUnOccupied(pos)) {
                SoundManager.attackSound();
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
            SoundManager.lastHitSound();
            CameraManager.dieScreenShake();
            SoundManager.knockSound();
            this.die();
        }
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
        }
        else if (type.equals("antibody")) {
            g = new Antibody(name);
            g.setGeneticCode(geneticCode.getCopy(g));
        }
        return g;
    }

    public int getMaxHP() {return maxHP;}
}
