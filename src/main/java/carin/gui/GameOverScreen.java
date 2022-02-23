package carin.gui;

import carin.Config;
import carin.Program;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GameOverScreen extends GameScreen {
    public static final String NAME = "GAMEOVER";
    public static final Color COLOR_OUTLINE = new Color(0, 0, 0, 180);
    private static final Color COLOR_BG = new Color(70, 70, 70, 255);
    private static final Color COLOR_SHOP = new Color(30, 30, 30, 255);
    private ImageComponent playButton;


    public GameOverScreen(){super(NAME);}

    @Override
    public void render(Graphics2D g) {
        renderLogoGameOver(g);
    }
    protected void initializeComponents() {

    }

    private void renderLogoGameOver(Graphics2D g) {
        double x = (Game.window().getCenter().getX());
        double y = (Game.window().getCenter().getY()-100);

        Rectangle2D bottomBg2 = new Rectangle2D.Double(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
        g.setColor(COLOR_SHOP);

        Rectangle2D bottomBg = new Rectangle2D.Double(40, 40, 1280, 691);
        g.setColor(COLOR_BG);

        ShapeRenderer.render(g, bottomBg);
        String gameOver = "GAME OVER!";
        g.setFont(Program.GUI_FONT.deriveFont(120f));
        g.setColor(Color.RED);
        TextRenderer.renderWithOutline(g, gameOver, x, y,COLOR_OUTLINE);
    }

//    private void renderExit(Graphics2D g) {
//
//    }
    private static void displayIngameScreen() {
        // gameplay screen
        Game.screens().display("MENU");
    }
}
