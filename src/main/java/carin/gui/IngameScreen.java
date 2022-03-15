package carin.gui;
import carin.GameStates;
import carin.LogicLoop;
import carin.util.SoundManager;
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

        super.initializeComponents();
        playButton = new ImageComponent(
                Game.window().getResolution().getWidth() - 80,
                Game.window().getResolution().getHeight() - 45,
                36,
                36);
        playButton.setImage(resume);

        playButton.onClicked(e -> {
            // toggle pause
            LogicLoop loop = GameStates.loop();
            if (!loop.isGameOver()) {
                SoundManager.tickSound();
                loop.setPause(!loop.isPause());
            }
        });

        this.getComponents().add(hud);
        this.getComponents().add(playButton);
    }

    public static void update(boolean isPause) {
        if (playButton != null)
            playButton.setImage(isPause ? resume : pause);
    }

    public static void resetPlayButton() {
        playButton.setImage(resume);
    }
}
