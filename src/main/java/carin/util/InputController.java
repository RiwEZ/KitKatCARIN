package carin.util;

import carin.Config;
import carin.GameStates;
import carin.entities.Antibody;
import de.gurkenlabs.litiengine.input.IKeyboard;
import de.gurkenlabs.litiengine.input.IMouse;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.geom.Point2D;

public class InputController {
    private static boolean spacebarToggle = true;
    private static Antibody currentFocus;
    private static Point2D mouseManual;
    private final IMouse.MousePressedListener movePressing;
    private final IMouse.MouseDraggedListener moveDragging;
    private final IMouse.MouseReleasedListener moveReleasing;
    private final IKeyboard.KeyPressedListener togglePause;
    private final IKeyboard.KeyReleasedListener spacebarReleased;
    private Point2D prevPos;
    private boolean isPressed;

    public InputController() {
        GameStates states = GameStates.states();

        movePressing = e -> {
            if (Input.keyboard().isPressed(17)) return;
            if (Input.mouse().isLeftButton(e)) {
                Point2D snap = states.getSnap(Input.mouse().getMapLocation());
                currentFocus = states.getFocusedAntibody(snap);
                if (currentFocus != null) {
                    prevPos = currentFocus.getLocation();
                    GameStates.loop().setPause(true);
                    InputController.setSpacebarToggle(false);
                    CameraManager.setCanDrag(false);
                }
            }
        };

        moveDragging = e -> entityOnCursor();

        moveReleasing = e -> {
            if (currentFocus != null && Input.mouse().isLeftButton(e)) {
                mouseManual = Input.mouse().getMapLocation();
                currentFocus.manualMove(prevPos, states.getSnap(mouseManual));
                InputController.setSpacebarToggle(true);
                CameraManager.setCanDrag(true);
                currentFocus = null;
            }
        };

        isPressed = false;
        togglePause = e -> {
            if (!isPressed && spacebarToggle) {
                GameStates.loop().setPause(!GameStates.loop().isPause());
                isPressed = true;
            }
        };
        spacebarReleased = e -> isPressed = false;
    }

    public static void entityOnCursor() {
        if (currentFocus != null) {
            mouseManual = Input.mouse().getMapLocation();
            double xOffset = Config.tile_width / 2.0;
            double yOffset = Config.tile_height / 2.0;
            Point2D pos = new Point2D.Double(mouseManual.getX() - xOffset, mouseManual.getY() - yOffset);
            currentFocus.setLocation(pos);
        }
    }

    public static void setSpacebarToggle(boolean value) {
        spacebarToggle = value;
    }

    public void initListener() {
        Input.mouse().onPressed(movePressing);
        Input.mouse().onDragged(moveDragging);
        Input.mouse().onReleased(moveReleasing);
        Input.keyboard().onKeyPressed(32, togglePause);
        Input.keyboard().onKeyReleased(32, spacebarReleased);
    }

    public void clearListener() {
        Input.mouse().removeMousePressedListener(movePressing);
        Input.mouse().removeMouseDraggedListener(moveDragging);
        Input.mouse().removeMouseReleasedListener(moveReleasing);
        Input.keyboard().removeKeyPressedListener(32, togglePause);
        Input.keyboard().removeKeyReleasedListener(32, spacebarReleased);
    }
}
