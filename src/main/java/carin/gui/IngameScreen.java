package carin.gui;


import carin.GameStates;
import carin.LogicLoop;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.image.BufferedImage;

public class IngameScreen extends GameScreen {
    public static final String NAME = "INGAME";
    private ImageComponent playButton;
    private static final BufferedImage resume = Resources.images().get("misc/resume.png");
    private static final BufferedImage pause = Resources.images().get("misc/pause.png");
    private Hud hud;

    public IngameScreen() {
        super(NAME);
    }

    @Override
    protected void initializeComponents() {
        hud = new Hud();

        // this is temporary pause button
        super.initializeComponents();
        this.playButton = new ImageComponent(Game.window().getResolution().getWidth() - 80, Game.window().getResolution().getHeight() - 45, 36, 36);
        this.playButton.setImage(resume);

        this.playButton.onClicked(e -> {
            // toggle pause
            if (!GameStates.loop().isGameOver()) {
                GameStates.loop().togglePause();
                if(GameStates.loop().isPause()){
                    this.playButton.setImage(resume);
                }
                else{
                    this.playButton.setImage(pause);
                }
            }
            else {
                GameStates.states().init();
            }
        });


        this.getComponents().add(hud);
        this.getComponents().add(this.playButton);
    }
}
