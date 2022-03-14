package carin.util;

import carin.Config;
import carin.GameStates;
import carin.entities.Antibody;
import de.gurkenlabs.litiengine.input.IMouse;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

public class InputController {
    private Antibody currentFocus;
    private Point2D prevPos;
    private Point2D mouseManual;
    private final IMouse.MousePressedListener movePressing;
    private final IMouse.MouseDraggedListener moveDragging;
    private final IMouse.MouseReleasedListener moveReleasing;

    public InputController() {
        GameStates states = GameStates.states();

        movePressing = e -> {
            Point2D snap = states.getSnap(Input.mouse().getMapLocation());
            currentFocus = states.getFocusedAntibody(snap);
            if (currentFocus != null) {
                prevPos = currentFocus.getLocation();
                GameStates.loop().setPause(true);
            }
        };

        moveDragging = e -> {
            mouseManual = Input.mouse().getMapLocation();
            double xOffset = Config.tile_width/2.0;
            double yOffset = Config.tile_height/2.0;
            Point2D pos = new Point2D.Double(mouseManual.getX() - xOffset, mouseManual.getY() - yOffset);
            if (currentFocus != null)
                currentFocus.setLocation(pos);
        };

        moveReleasing = e -> {
            if (currentFocus != null) {
                currentFocus.manualMove(prevPos, states.getSnap(mouseManual));
            }
        };
    }

    public void initManualMove() {
        Input.mouse().onPressed(movePressing);
        Input.mouse().onDragged(moveDragging);
        Input.mouse().onReleased(moveReleasing);
    }

    public void clearManualMove() {
        Input.mouse().removeMousePressedListener(movePressing);
        Input.mouse().removeMouseDraggedListener(moveDragging);
        Input.mouse().removeMouseReleasedListener(moveReleasing);
    }


}
