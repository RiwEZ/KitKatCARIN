package carin.gui;

import carin.GameStates;
import carin.LogicLoop;
import carin.Program;
import carin.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.graphics.animation.AnimationController;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Hud extends GuiComponent {
    public static final Color COLOR_OUTLINE = new Color(0, 0, 0, 180);
    private static final Color COLOR_BG = new Color(70, 70, 70, 255);
    private static final Color COLOR_BAD = new Color(247, 152, 0, 220);
    private static final Color COLOR_AVG = new Color(252, 248, 118, 220);
    private static final Color COLOR_GOOD = new Color(143, 212, 61, 220);
    private static final int PADDING = 10;

    private ImageComponent speedUp;

    private AnimationController logoAnimationController;

    public Hud() {
        super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
    }

    @Override
    public void render(final Graphics2D g){
        this.renderGameInfo(g);
        super.render(g);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        double x = Game.window().getCenter().getX() / 2;
        double y = Game.window().getHeight() - 100;
        this.speedUp = new ImageComponent(x, y, 100, 100, "x2");
        this.speedUp.setFont(Program.GUI_FONT.deriveFont(50f));
        this.speedUp.getAppearance().setForeColor(new Color(215, 82, 82));
        this.speedUp.onClicked(e -> {
            if (this.speedUp.getText().equals("x2")) {
                LogicLoop.instance().setXSpeed(2);
                this.speedUp.setText("x1");
            }
            else {
                LogicLoop.instance().setXSpeed(1);
                this.speedUp.setText("x2");
            }
        });

        this.getComponents().add(speedUp);
    }

    private void renderGameInfo(Graphics2D g) {
        g.setFont(Program.GUI_FONT_SMALL2);

        FontMetrics fm = g.getFontMetrics();
        double bgHeight = fm.getHeight() + PADDING * 2;
        double bgY = Game.window().getResolution().getHeight() - 2*bgHeight;
        double textY = bgY + fm.getHeight() * 1.1;

        // render bottom HUD
        Rectangle2D bottomBg = new Rectangle2D.Double(0, bgY, Game.window().getResolution().getWidth(), 60);
        g.setColor(COLOR_BG);
        ShapeRenderer.render(g, bottomBg);

        // render top HUD
        // render bottom HUD
        Rectangle2D topBg = new Rectangle2D.Double(0, 0, Game.window().getResolution().getWidth(), 36);
        g.setColor(COLOR_BG);
        ShapeRenderer.render(g, topBg);

        g.setColor(Color.WHITE);

        // render Antibody, Virus number
        String numberAnti = "Antibody: " + GameStates.states().getAntibodyCount();
        String numberVirus = "Virus: " + GameStates.states().getVirusCount();
        String numberCredit = "Credit: " + Player.instance().getCredit();
        double numberX =  PADDING * 10;
        TextRenderer.renderWithOutline(g, numberVirus, numberX, textY+12, COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, numberAnti, numberX, textY+28,COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, numberCredit, numberX + 120, textY+12,COLOR_OUTLINE);

    }
}
