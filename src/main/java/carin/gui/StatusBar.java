package carin.gui;

import carin.Program;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StatusBar implements IRenderable {
    private final Virus entity;

    public StatusBar(Virus entity) {
        this.entity = entity;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Program.GUI_FONT_SMALLEST);
        int ID = entity.getID();
        int HP = entity.getHP();
        double x = entity.getCenter().getX();
        double y = entity.getCenter().getY();
        String textID = "ID " + String.valueOf(ID);
        String textHP = "HP " + String.valueOf(HP);
        String textLoc = "X " + String.valueOf(x) + " Y " + String.valueOf(y);
        TextRenderer.render(g, textID, entity.getLocation().getX() + 255, entity.getLocation().getY() + 10);
        TextRenderer.render(g, textHP, entity.getLocation().getX() + 255, entity.getLocation().getY());
        TextRenderer.render(g, textLoc, entity.getLocation().getX() + 255, entity.getLocation().getY() - 10);
    }
}
