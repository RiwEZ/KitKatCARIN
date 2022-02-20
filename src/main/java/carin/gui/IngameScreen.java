package carin.gui;

import carin.GameStates;
import carin.Program;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class IngameScreen extends GameScreen {
    public static final String NAME = "INGAME";
    private ImageComponent playButton;
    public static boolean isPause = false;
    private Hud hud;

    public IngameScreen() {
        super(NAME);
    }

    @Override
    protected void initializeComponents() {
        hud = new Hud();

        // this is temporary pause button
        super.initializeComponents();
        double x = Game.window().getCenter().getX() / 2;
        double y = Game.window().getCenter().getY() / 2;
        double width = Game.window().getResolution().getWidth() / 4;
        double height = Game.window().getResolution().getWidth() / 6;
        this.playButton = new ImageComponent(x - width / 2.0, y + height / 3.0, width, height);
        this.playButton.setImage(null);
        this.playButton.setText("RESUME");
        this.playButton.setFont(Program.GUI_FONT.deriveFont(100f));
        this.playButton.getAppearance().setForeColor(new Color(215, 82, 82));
        this.playButton.getAppearanceHovered().setForeColor(new Color(253, 184, 184));

        this.playButton.onClicked(e -> {
            //Game.world().camera().setZoom(0.1f, 0);
            // toggle pause
            if (!GameStates.states().logicLoop().isGameOver()) {
                GameStates.states().logicLoop().togglePause();
                if(GameStates.states().logicLoop().isPause()){
                    this.playButton.setText("RESUME");
                }
                else{
                    this.playButton.setText("PAUSE");
                }
            }
        });


        this.getComponents().add(this.playButton);
        this.getComponents().add(hud);
    }
}
