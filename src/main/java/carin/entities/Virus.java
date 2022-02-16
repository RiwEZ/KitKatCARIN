package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.input.Input;

import java.util.Collection;

@EntityInfo(width = 36, height = 36)
@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = true)
public class Virus extends Creature implements GeneticEntity, IUpdateable {
    Collection<MapArea> allArea = GameStates.getMapArea();
    private final int maxHP = Config.virus_hp;
    private final int damage = Config.virus_dmg;
    private int currentHP;
    private static int ID = 0;
    private int virusID;

    public Virus() {
        super("virus");
        this.currentHP = maxHP;
        this.virusID = ID;
        ID++;
        addEntityRenderListener(e -> new StatusBar(this).render(e.getGraphics()));
    }

    @Override
    public void update() {
        if(Input.mouse().isRightButtonPressed()){
            moveRight();
        }
        else if(Input.mouse().isLeftButtonPressed()){
            moveLeft();
        }
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
