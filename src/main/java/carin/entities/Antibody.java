package carin.entities;

import carin.Config;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.input.Input;

@EntityInfo(width = 36, height = 36)
@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = true)
public class Antibody extends Creature implements GeneticEntity, IUpdateable {
    private final int maxHP = Config.antibody_hp;
    private int currentHP;
    private static int ID = 0;
    private int antibodyID;
    public Antibody() {
        super("antibody");
        this.currentHP = this.maxHP;
        this.antibodyID = ID;
        ID++;
    }

    @Override
    public void update(){
        if(Input.mouse().isRightButtonPressed()){
            moveRight();
        }
        else if(Input.mouse().isLeftButtonPressed()){
            moveLeft();
        }
    }

    public int getID() {
        return antibodyID;
    }

    public int getHP(){
        return currentHP;
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
