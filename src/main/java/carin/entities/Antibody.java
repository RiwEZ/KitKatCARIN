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
    }

    @Override
    public void run() {
    }

    @Override
    public void move(double x, double y) {
    }

    public int getID() {
        return antibodyID;
    }

    public int getHP(){
        return currentHP;
    }

}
