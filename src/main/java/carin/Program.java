package carin;

import carin.gui.GameOverScreen;
import carin.gui.IngameScreen;
import carin.gui.MenuScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Program {
    public static final Font GUI_FONT = Resources.fonts().get("misc/ARCADECLASSIC.TTF").deriveFont(48f);
    public static final Font GUI_FONT_SMALL = GUI_FONT.deriveFont(30f);
    public static final Font GUI_FONT_SMALLEST = Resources.fonts().get("misc/Kanit-Thin.ttf").deriveFont(8f);
    public static final Font GUI_FONT_SMALL2 = Resources.fonts().get("misc/PressStart2P-vaV7.ttf").deriveFont(8f);
    public static final Dimension size = new Dimension(1382,807);
    public static final BufferedImage Cursor = Resources.images().get("misc/cursor.png");

    public static void main(String[] args) {
        GuiProperties.setDefaultFont(GUI_FONT_SMALL2.deriveFont(10f));
        Resources.load("game.litidata");
        Game.info().setName("C.A.R.I.N | by KitKat");
        Game.info().setSubTitle("");
        Game.info().setWebsite("https://github.com/RiwEZ/KitKatCARIN");
        Game.init(args);
        GameStates.states().init();
        Game.window().getRenderComponent().fadeIn(1000);
        Game.screens().add(new MenuScreen());
        Game.screens().add(new IngameScreen());
        Game.screens().add(new GameOverScreen());
        Game.start();
        Game.window().cursor().set(Cursor);
        // Prevent Resize Game window
        Game.window().getHostControl().setMinimumSize(size);
        Game.window().onResolutionChanged(res -> {
            Game.window().getHostControl().setSize(size);
        });
    }

}
