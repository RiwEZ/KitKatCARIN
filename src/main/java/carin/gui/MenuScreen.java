package carin.gui;

import carin.GameStates;
import carin.Program;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuScreen extends Screen {

    private static final BufferedImage MENU_LOGO = Resources.images().get("misc/MENU_LOGO.png");

    public static final String NAME = "MENU";
    private ImageComponent playButton;
    private ImageComponent exitButton;
    private boolean locked;

    public MenuScreen() {
        super(NAME);
    }

    @Override
    public void render(final Graphics2D g) {

        renderGameLogo(g);

        // render info
        String info = "by KitKat group";
        g.setFont(Program.GUI_FONT_SMALL);
        g.setColor(Color.WHITE);
        double infoX1 = Game.window().getCenter().getX() - (g.getFontMetrics().stringWidth(info) + 12) / 2.0 ;
        double infoY1 = Game.window().getResolution().getHeight() - g.getFontMetrics().getHeight() - 10;
        TextRenderer.render(g, info, infoX1, infoY1);

        super.render(g);
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        double x = Game.window().getCenter().getX();
        double y = Game.window().getCenter().getY();
        double width = Game.window().getResolution().getWidth() / 3;
        double height = Game.window().getResolution().getWidth() / 6;
        this.playButton = new ImageComponent(x - width / 2.0, y - height / 3.0, width, height);
        this.playButton.setImage(null);
        this.playButton.setText("START");
        this.playButton.setFont(Program.GUI_FONT.deriveFont(100f));
        this.playButton.getAppearance().setForeColor(new Color(215, 82, 82));
        this.playButton.getAppearanceHovered().setForeColor(new Color(253, 184, 184));

        this.playButton.onClicked(e -> {
            if (this.locked) {
                return;
            }
            this.locked = true;
            this.playButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                GameStates.states().init();
                IngameScreen.resetPlayButton();
                Hud.resetSpeedSlide();
                Game.screens().display("INGAME");
                Game.window().getRenderComponent().fadeIn(1000);
                this.locked = false;
                this.playButton.setEnabled(true);
            });
        });
        this.getComponents().add(this.playButton);

        // exit button
        this.exitButton = new ImageComponent(x - width / 2.0, y + height / 6.0, width, height);
        this.exitButton.setImage(null);
        this.exitButton.setText("EXIT");
        this.exitButton.setFont(Program.GUI_FONT.deriveFont(75f));
        this.exitButton.getAppearance().setForeColor(new Color(215, 82, 82));
        this.exitButton.getAppearanceHovered().setForeColor(new Color(253, 184, 184));

        this.exitButton.onClicked(e -> {
            System.exit(0);
        });
        this.getComponents().add(this.exitButton);
    }

    public static void renderGameLogo(Graphics2D g) {
        // render game logo
        final int defaultSize = 73;
        final double logoScale = 7;
        final double size = defaultSize * logoScale;
        double x = (Game.window().getCenter().getX() - size) + 60;
        double y = (Game.window().getCenter().getY() - size) + 50;
        ImageRenderer.renderScaled(g, MENU_LOGO, x, y, logoScale);
    }

}
