package carin.gui;

import carin.GameStates;
import carin.LogicLoop;
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

    private ImageComponent restartButton;
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
        // restart button
        double x = Game.window().getCenter().getX();
        double y = Game.window().getCenter().getY();
        double width = Game.window().getResolution().getWidth() / 3;
        double height = Game.window().getResolution().getWidth() / 6;
        this.restartButton = new ImageComponent(x - width / 2.0, y - height / 3.0, width, height);
        this.restartButton.setImage(null);
        this.restartButton.setText("RESTART");
        this.restartButton.setFont(Program.GUI_FONT.deriveFont(80f));
        this.restartButton.getAppearance().setForeColor(new Color(255,255,255));
        this.restartButton.getAppearanceHovered().setForeColor(new Color(215, 82, 82));

        this.restartButton.onClicked(e -> {
            if (this.locked) {
                return;
            }
            this.locked = true;
            this.restartButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                GameStates.states().init();
                IngameScreen.resetPlayButton();
                Hud.resetSpeedSlide();
                Game.screens().display("INGAME");
                Game.window().getRenderComponent().fadeIn(1000);
                this.locked = false;
                this.restartButton.setEnabled(true);
            });
        });
        this.getComponents().add(this.restartButton);

        // exit button
        ImageComponent exitButton = new ImageComponent(x - width / 2.0, y + height / 6.0, width, height);
        exitButton.setImage(null);
        exitButton.setText("EXIT");
        exitButton.setFont(Program.GUI_FONT.deriveFont(75f));
        exitButton.getAppearance().setForeColor(new Color(255,255,255));
        exitButton.getAppearanceHovered().setForeColor(new Color(215, 82, 82));

            Game.window().getRenderComponent().fadeOut(1000);
            Game.screens().display("MENU");
            Game.window().getRenderComponent().fadeIn(1000);

        exitButton.onClicked(e -> {
            if (this.locked) {
                return;
            }
            this.locked = true;
            exitButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                Game.screens().display("MENU");
                Game.window().getRenderComponent().fadeIn(1000);
                this.locked = false;
                exitButton.setEnabled(true);
            });
        });
        this.getComponents().add(exitButton);
    }

    private void renderGameOverLogo(Graphics2D g) {
        double x = (Game.window().getCenter().getX());
        double y = (Game.window().getCenter().getY()-100);

        Rectangle2D greyBg = new Rectangle2D.Double(40, 40, 1280, 691);
        g.setColor(COLOR_BG);
        String endWith;
        ShapeRenderer.render(g, greyBg);
        if(LogicLoop.isPlayerWin){
            endWith = "Player win!";
            g.setColor(Color.GREEN);
        }
        else {
            endWith = "Game Over!";
            g.setColor(Color.RED);
        }
        g.setFont(Program.GUI_FONT.deriveFont(150f));
        TextRenderer.renderWithOutline(g, endWith, x, y,COLOR_OUTLINE);
    }

}
