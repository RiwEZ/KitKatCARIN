package carin.gui;

import carin.Program;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.Screen;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GameOverScreen extends Screen {
    public static final String NAME = "GAMEOVER";
    public static final Color COLOR_OUTLINE = new Color(0, 0, 0, 180);
    private static final Color COLOR_BG = new Color(70, 70, 70, 255);
    private static final Color COLOR_SHOP = new Color(30, 30, 30, 255);
    private ImageComponent playButton;
    private boolean locked;

    public GameOverScreen(){super(NAME);}

    @Override
    public void render(final Graphics2D g) {
        renderGameOverLogo(g);

        super.render(g);
    }
    @Override
    public void prepare() {
        super.prepare();
    }
    protected void initializeComponents() {
        super.initializeComponents();
        // exit button
        double x = Game.window().getCenter().getX();
        double y = Game.window().getCenter().getY();
        double width = Game.window().getResolution().getWidth() / 3;
        double height = Game.window().getResolution().getWidth() / 6;
        this.playButton = new ImageComponent(x - width / 2.0, y + height / 3.0, width, height);
        this.playButton.setImage(null);
        this.playButton.setText("EXIT");
        this.playButton.setFont(Program.GUI_FONT.deriveFont(100f));
        this.playButton.getAppearance().setForeColor(new Color(255,255,255));
        this.playButton.getAppearanceHovered().setForeColor(new Color(215, 82, 82));

        this.playButton.onClicked(e -> {
            if (this.locked) {
                return;
            }
            this.locked = true;
            this.playButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                displayMenuScreen();
                Game.window().getRenderComponent().fadeIn(1000);
                this.locked = false;
                this.playButton.setEnabled(true);
            });
        });
        this.getComponents().add(this.playButton);
    }

    private void renderGameOverLogo(Graphics2D g) {
        double x = (Game.window().getCenter().getX());
        double y = (Game.window().getCenter().getY()-100);

        Rectangle2D greyBg = new Rectangle2D.Double(40, 40, 1280, 691);
        g.setColor(COLOR_BG);

        ShapeRenderer.render(g, greyBg);

        String gameOver = "GAME OVER!";
        g.setFont(Program.GUI_FONT.deriveFont(150f));
        g.setColor(Color.RED);
        TextRenderer.renderWithOutline(g, gameOver, x, y,COLOR_OUTLINE);
    }

    private static void displayMenuScreen() {
        // menu screen
        Game.screens().display("MENU");
    }
}
