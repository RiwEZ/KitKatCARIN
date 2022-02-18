package carin.entities;

import carin.Config;
import carin.GameStates;
import carin.gui.StatusBar;
import carin.parser.GeneticParser;
import carin.parser.GeneticProgram;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.*;

import java.awt.geom.Point2D;

@EntityInfo(width = 36, height = 36)
@CollisionInfo(collisionBoxWidth = 36, collisionBoxHeight = 36, collision = true)
public class Antibody extends Creature implements GeneticEntity, IUpdateable {

    private final int maxHP = Config.antibody_hp;
    private int currentHP;
    private static int ID = 0;
    private int antibodyID;
    private final GeneticProgram geneticCode;
    private final Controller entityController;

    public Antibody() {
        super("antibody");
        this.currentHP = this.maxHP;
        this.antibodyID = ID;
        ID++;
        addEntityRenderListener(e -> new StatusBar(this).render(e.getGraphics()));
        geneticCode = new GeneticParser(GameStates.states(), this, "tests/antibody1.in").getProgram();
        // add Controller
        entityController = new Controller(this, GameStates.states());
    }

    @Override
    public void update(){
    }

    @Override
    public void run() {
        geneticCode.run();
    }

    @Override
    public Point2D location() {
        return this.getLocation();
    }

    @Override
    public void move(double x, double y) {
        entityController.move(x,y);
    }

    public int getID() {
        return antibodyID;
    }

    public int getHP(){
        return currentHP;
    }

}
