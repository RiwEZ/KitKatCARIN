package carin;

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
    public static float HUD_SCALE = 2.0f;

    public static void main(String[] args) {
        Game.info().setName("C.A.R.I.N | by KitKat");
        Game.info().setSubTitle("");
        Game.info().setWebsite("https://github.com/RiwEZ/KitKatCARIN");
        Game.init(args);
        initCARIN();
        Game.start();
    }

    private static void initCARIN() {
        // init default UI settings
        Game.window().getRenderComponent().fadeIn(1000);
        GuiProperties.setDefaultFont(GUI_FONT);
        Game.screens().add(new MenuScreen());
        Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> {
            System.exit(0);
        });
    }
}
