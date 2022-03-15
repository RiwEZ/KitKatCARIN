package carin.gui;

import carin.GameStates;
import carin.LogicLoop;
import carin.Program;
import carin.util.SoundManager;
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
        this.restartButton = new ImageComponent(x - width / 2.0, y - height / 8.0, width, height / 2.0);
        this.restartButton.setImage(null);
        this.restartButton.setText("RESTART");
        this.restartButton.setFont(Program.GUI_FONT.deriveFont(80f));
        this.restartButton.getAppearance().setForeColor(new Color(255,255,255));
        this.restartButton.getAppearanceHovered().setForeColor(new Color(255, 35, 35));

        this.restartButton.onClicked(e -> {
            Game.audio().stopMusic();
            if (this.locked) {
                return;
            }
            this.locked = true;
            this.restartButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                Hud.antibodyShop.changeZero();
                GameStates.states().init();
                IngameScreen.resetPlayButton();
                Hud.resetSpeedSlide();
                Game.screens().display("INGAME");
                Game.window().getRenderComponent().fadeIn(1000);
                SoundManager.ingame();
                this.locked = false;
                this.restartButton.setEnabled(true);
            });
        });
        this.getComponents().add(this.restartButton);

        // exit button
        ImageComponent exitButton = new ImageComponent(x - width / 2.0, y + height / 2.0, width, height / 2.0);
        exitButton.setImage(null);
        exitButton.setText("EXIT");
        exitButton.setFont(Program.GUI_FONT.deriveFont(75f));
        exitButton.getAppearance().setForeColor(new Color(255,255,255));
        exitButton.getAppearanceHovered().setForeColor(new Color(255, 35, 35));

            Game.window().getRenderComponent().fadeOut(1000);
            Game.screens().display("MENU");
            Game.window().getRenderComponent().fadeIn(1000);

        exitButton.onClicked(e -> {
            Game.audio().stopMusic();
            if (this.locked) {
                return;
            }
            this.locked = true;
            exitButton.setEnabled(false);
            Game.window().getRenderComponent().fadeOut(1000);
            Game.loop().perform(1500, () -> {
                Hud.antibodyShop.changeZero();
                Game.screens().display("MENU");
                Game.window().getRenderComponent().fadeIn(1000);
                SoundManager.menu();
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
