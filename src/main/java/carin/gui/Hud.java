package carin.gui;

import carin.Config;
import carin.GameStates;
import carin.Program;
import carin.entities.Player;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.ShapeRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.HorizontalSlider;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Hud extends GuiComponent {
    public static final Color COLOR_OUTLINE = new Color(0, 0, 0, 180);
    private static final Color COLOR_BG = new Color(70, 70, 70, 255);
    private static final Color COLOR_SHOP = new Color(30, 30, 30, 255);
    private static final int PADDING = 10;
    private static HorizontalSlider speedSlider;

    public Hud() {
        super(0, 0, Game.window().getResolution().getWidth(), Game.window().getResolution().getHeight());
    }

    public static void resetSpeedSlide() {
        speedSlider.setCurrentValue(1f);
    }

    @Override
    public void render(final Graphics2D g) {
        this.renderShop(g);
        this.renderGameInfo(g);
        super.render(g);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        speedSlider = new HorizontalSlider(Game.window().getResolution().getWidth() - 200, Game.window().getResolution().getHeight() - 27, 90, 16,
                1, 3, 1);
        speedSlider.setShowTicks(true);
        speedSlider.onChange(c -> GameStates.loop().setXSpeed(Math.max(c.intValue(), 1)));

        AntibodyShop antibodyShop = new AntibodyShop();

        this.getComponents().add(speedSlider);
        this.getComponents().add(antibodyShop);
        this.getComponents().add(antibodyShop.getDdList());
    }

    private void renderGameInfo(Graphics2D g) {
        g.setFont(Program.GUI_FONT_SMALL2);

        FontMetrics fm = g.getFontMetrics();
        double bgHeight = fm.getHeight() + PADDING * 2;
        double bgY = Game.window().getResolution().getHeight() - 2 * bgHeight;
        double textY = bgY + fm.getHeight() * 1.1;

        // render bottom HUD
        Rectangle2D bottomBg = new Rectangle2D.Double(0, bgY, Game.window().getResolution().getWidth(), 60);
        g.setColor(COLOR_BG);
        ShapeRenderer.render(g, bottomBg);

        // render top HUD
        Rectangle2D topBg = new Rectangle2D.Double(0, 0, Game.window().getResolution().getWidth(), 36);
        g.setColor(COLOR_BG);
        ShapeRenderer.render(g, topBg);

        g.setColor(Color.WHITE);

        // render Antibody, Virus number
        String numberAnti = "ANTIBODY: " + GameStates.states().getAntibodyCount();
        String numberVirus = "VIRUS: " + GameStates.states().getVirusCount();
        String numberCredit = "CREDIT: " + Player.instance().getCredit() + "$";
        String tick = "TIME: " + GameStates.loop().getTick();
        //String initTime = "InitTime: " + GameStates.states().initTime();
        String speedControl = "SPEED CONTROL";
        g.setFont(Program.GUI_FONT_SMALL2.deriveFont(12f));
        TextRenderer.renderWithOutline(g, numberVirus, 200, textY + 8, COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, numberAnti, 200, textY + 29, COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, numberCredit, 200, 17, COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, tick, 470, Game.window().getResolution().getHeight() - 70, COLOR_OUTLINE);
        g.setFont(Program.GUI_FONT_SMALL2);
        TextRenderer.renderWithOutline(g, speedControl, getWidth() - 155, textY + 7, COLOR_OUTLINE);

    }

    private void renderShop(Graphics2D g) {
        Rectangle2D shopBg = new Rectangle2D.Double(0, 0, 400, Game.window().getResolution().getHeight());
        g.setColor(COLOR_SHOP);
        ShapeRenderer.render(g, shopBg);
        g.setFont(Program.GUI_FONT_SMALL2.deriveFont(30f));
        g.setColor(Color.WHITE);
        String header = "SHOP";
        TextRenderer.renderWithOutline(g, header, shopBg.getCenterX(), 100, COLOR_OUTLINE);
        g.setFont(Program.GUI_FONT_SMALL2);
        String moveCost = "MANUAL MOVE COST: " + Config.move_hp_cost + " HP";
        String placeCost = "PLACEMENT COST: " + Config.placement_cost + " $";
        TextRenderer.renderWithOutline(g, moveCost, shopBg.getCenterX(), 300, COLOR_OUTLINE);
        TextRenderer.renderWithOutline(g, placeCost, shopBg.getCenterX(), 320, COLOR_OUTLINE);
        String SelectGC = "SELECT GENETIC CODE";
        g.setFont(Program.GUI_FONT_SMALL2.deriveFont(18f));
        TextRenderer.renderWithOutline(g, SelectGC, shopBg.getCenterX(), 400, COLOR_OUTLINE);

    }

}
