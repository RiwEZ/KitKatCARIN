package carin;

import carin.gui.IngameScreen;
import carin.gui.MenuScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Program {
    public static final Font GUI_FONT = Resources.fonts().get("misc/ARCADECLASSIC.TTF").deriveFont(48f);
    public static final Font GUI_FONT_SMALL = GUI_FONT.deriveFont(30f);

    public static void main(String[] args) {
        GuiProperties.setDefaultFont(GUI_FONT);
        Resources.load("game.litidata");
        Game.info().setName("C.A.R.I.N | by KitKat");
        Game.info().setSubTitle("");
        Game.info().setWebsite("https://github.com/RiwEZ/KitKatCARIN");
        Game.init(args);
        GameStates.init();
        Game.world().loadEnvironment("map1");
        Game.window().getRenderComponent().fadeIn(1000);
        Game.screens().add(new MenuScreen());
        Game.screens().add(new IngameScreen());
        Game.start();
    }

}
