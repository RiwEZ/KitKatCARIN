package carin.gui;

import carin.Program;
import carin.entities.Antibody;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StatusBar implements IRenderable {
    private final Creature entity;
    private int ID;
    private double HP;
    private double maxHP;
    private double fac;

    public StatusBar(Creature entity) {
        this.entity = entity;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Program.GUI_FONT_SMALLEST);
        if(entity.getClass().equals(Virus.class)){
            ID = ((Virus) entity).getID();
            HP = ((Virus) entity).getCurrHP();
            maxHP = ((Virus) entity).getMaxHP();
            fac = HP / maxHP;
            String image = Math.round(fac * 10) * 10 + "-hp";
            BufferedImage bar = Resources.images().get("sprites/hpbar/virus/" + image + ".png");
            g.drawImage(bar, (int) (Game.world().camera().getViewportLocation(entity.getCenter()).getX() - (bar.getWidth() / 2.0)), (int) (Game.world().camera().getViewportLocation(entity.getCenter()).getY() - 22), (bar.getWidth()), (bar.getHeight()), null);
        }
        else if(entity.getClass().equals(Antibody.class)){
            ID = ((Antibody) entity).getID();
            HP = ((Antibody) entity).getCurrHP();
            maxHP = ((Antibody) entity).getMaxHP();
            fac = HP / maxHP;
            String image = Math.round(fac * 10) * 10 + "-hp";
            BufferedImage bar = Resources.images().get("sprites/hpbar/antibody/" + image + ".png");
            g.drawImage(bar, (int) (Game.world().camera().getViewportLocation(entity.getCenter()).getX() - (bar.getWidth() / 2.0)), (int) (Game.world().camera().getViewportLocation(entity.getCenter()).getY() - 22), (bar.getWidth()), (bar.getHeight()), null);
        }
    }
}
