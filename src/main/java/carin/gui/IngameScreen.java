package carin.gui;


import carin.GameStates;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.image.BufferedImage;

public class IngameScreen extends GameScreen {
    public static final String NAME = "INGAME";
    private static ImageComponent playButton;
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
        playButton = new ImageComponent(Game.window().getResolution().getWidth() - 80, Game.window().getResolution().getHeight() - 45, 36, 36);
        playButton.setImage(resume);

        playButton.onClicked(e -> {
            // toggle pause
            if (!GameStates.loop().isGameOver()) {
                GameStates.loop().togglePause();
                if(GameStates.loop().isPause()){
                    playButton.setImage(resume);
                }
                else{
                    playButton.setImage(pause);
                }
            }
        });


        this.getComponents().add(hud);
        this.getComponents().add(playButton);
    }

    public static void resetPlayButton() {
        playButton.setImage(resume);
    }
}
