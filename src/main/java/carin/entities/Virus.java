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
@MovementInfo(velocity = 0)
public class Virus extends Creature implements GeneticEntity, IUpdateable {

    private final int maxHP = Config.virus_hp;
    private final int damage = Config.virus_dmg;
    private int currentHP;
    private static int ID = 0;
    private int virusID;
    private final GeneticProgram geneticCode;
    private final Controller entityController;

    public Virus() {
        super("virus");
        this.currentHP = maxHP;
        this.virusID = ID;
        ID++;
        addEntityRenderListener(e -> new StatusBar(this).render(e.getGraphics()));
        geneticCode = new GeneticParser(GameStates.states(), this, "tests/virus1.in").getProgram();
        // add Controller
        entityController = new Controller(this, GameStates.states());
    }

    @Override
    public void update() {
        //if (false) geneticCode.run();
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
        return virusID;
    }

    public int getHP() {
        return currentHP;
    }

    public int getDamage() {
        return damage;
    }
}
