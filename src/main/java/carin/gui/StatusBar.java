package carin.gui;

import carin.Program;
import carin.entities.Antibody;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;

import java.awt.*;

public class StatusBar implements IRenderable {
    private final Creature entity;
    private int ID;
    private int HP;

    public StatusBar(Creature entity) {
        this.entity = entity;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Program.GUI_FONT_SMALLEST);
        if(entity.getClass().equals(Virus.class)){
            ID = ((Virus) entity).getID();
            HP = ((Virus) entity).getHP();
        }
        else if(entity.getClass().equals(Antibody.class)){
            ID = ((Antibody) entity).getID();
            HP = ((Antibody) entity).getHP();
        }
        int x = (int) entity.getCenter().getX();
        int y = (int) entity.getCenter().getY();
        String textID = "ID " + String.valueOf(ID);
        String textHP = "HP " + String.valueOf(HP);
        String textLoc = "X " + String.valueOf(x) + " Y " + String.valueOf(y);
        TextRenderer.render(g, textID, (entity.getLocation().getX() - Game.world().camera().getFocus().getX() + 298  / Game.world().camera().getZoom()), (entity.getLocation().getY() - Game.world().camera().getFocus().getY() + 195 / Game.world().camera().getZoom()));
        TextRenderer.render(g, textHP, (entity.getLocation().getX() - Game.world().camera().getFocus().getX() + 298 / Game.world().camera().getZoom()), (entity.getLocation().getY() - Game.world().camera().getFocus().getY() + 185 / Game.world().camera().getZoom()));
        TextRenderer.render(g, textLoc, (entity.getLocation().getX() - Game.world().camera().getFocus().getX() + 298 / Game.world().camera().getZoom()), (entity.getLocation().getY() - Game.world().camera().getFocus().getY() + 175 / Game.world().camera().getZoom()));
    }
}
