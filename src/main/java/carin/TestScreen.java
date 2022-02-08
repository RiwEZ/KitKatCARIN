package carin;

import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class TestScreen extends GameScreen {
    public TestScreen() {
        super("TEST");
    }
    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        TextRenderer.render(g, "Hello World", 100, 100);
    }
}
