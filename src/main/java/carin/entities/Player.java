package carin.entities;

import carin.Config;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;

import java.util.ArrayList;


@EntityInfo(width = 16, height = 16)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 8, collisionBoxHeight = 16, collision = false)
public class Player extends Creature implements IUpdateable {
    private int initialCredit = Config.initial_credits;
    private int currentCredit;
    private ArrayList<Antibody> antibodyList = new ArrayList<>();

    private static Player instance;

    public static Player instance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {
        super("");
        this.currentCredit = initialCredit;
    }

    @Override
    public void update() {

    }

    public int getCredit() {
        return currentCredit;
    }

    public void changeCredit(int change) {
        currentCredit += change;
    }

    public void addAntibody(Antibody antibody) {
        this.antibodyList.add(antibody);
    }

    public ArrayList<Antibody> getAntibodyList() {
        return antibodyList;
    }



}
