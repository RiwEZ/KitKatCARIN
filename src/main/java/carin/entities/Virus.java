package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.configuration.ClientConfiguration;
import de.gurkenlabs.litiengine.entities.*;

import java.util.Collection;

@EntityInfo(width = 36, height = 36)
@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = true)
@MovementInfo(velocity = 0)
public class Virus extends Creature implements GeneticEntity, IUpdateable {
    Collection<MapArea> allArea = GameStates.getMapArea();
    private final int maxHP = Config.virus_hp;
    private final int damage = Config.virus_dmg;
    private int currentHP;
    private static int ID = 0;
    private int virusID;
    private final GeneticProgram geneticCode;

    public Virus() {
        super("virus");
        this.currentHP = maxHP;
        this.virusID = ID;
        ID++;
        addEntityRenderListener(e -> new StatusBar(this).render(e.getGraphics()));
        geneticCode = new GeneticParser(null, this, "tests/virus1.in").getProgram();
    }

    @Override
    public void update() {
        geneticCode.run();
        /*
        if(Input.mouse().isRightButtonPressed()){
            moveRight();
        }
        else if(Input.mouse().isLeftButtonPressed()){
            moveLeft();
        }
         */
    }

    @Override
    public void move(double x, double y) {
        this.setLocation(this.getX() + x*3, this.getY() + y*3);
    }

    public int getID() {
        return virusID;
    }

    public int getHP() {
        return currentHP;
    }

    public int getDamage() {
        return damage;
    }

    public void moveLeft() {
        this.getLocation().setLocation(this.getX() - 36, this.getY());
    }

    public void moveRight() {
        this.getLocation().setLocation(this.getX() + 36, this.getY());
    }

    public void moveUp() {
        this.getLocation().setLocation(this.getX(), this.getY() + 36);
    }

    public void moveDown() {
        this.getLocation().setLocation(this.getX(), this.getY() - 36);
    }

    public void moveUpRight() {
        this.getLocation().setLocation(this.getX() + 36, this.getY() + 36);
    }

    public void moveUpLeft() {
        this.getLocation().setLocation(this.getX() - 36, this.getY() + 36);
    }

    public void moveDownRight() {
        this.getLocation().setLocation(this.getX() + 36, this.getY() - 36);
    }

    public void moveDownLeft() {
        this.getLocation().setLocation(this.getX() - 36, this.getY() - 36);
    }
}
