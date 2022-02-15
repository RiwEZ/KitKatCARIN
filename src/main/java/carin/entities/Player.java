package carin.entities;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.input.PlatformingMovementController;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.util.ArrayList;


@EntityInfo(width = 16, height = 16)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 8, collisionBoxHeight = 16, collision = true)
public class Player extends Creature implements IUpdateable {
    private int credit = 1000;
    private ArrayList<Antibody> antibodys = new ArrayList<>();
    private static Player instance;
    public static Player instance() {
        if (instance == null) {
            instance = new Player();
        }

        return instance;
    }

    private Player() {
        super("");
    }

    public int getCredit() {
        return credit;
    }

    public void changeCredit(int change) {
        credit += change;
    }

    public void addAntibody(Antibody antibody) {
        this.antibodys.add(antibody);
    }

    @Override
    public void update() {
    }

}
