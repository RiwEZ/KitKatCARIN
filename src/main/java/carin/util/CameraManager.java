package carin.util;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.geom.Point2D;


public class CameraManager {
    private static final Camera camera = new Camera();
    private static float zoomAmount;
    private static float MaxZoomOut;
    private static float defaultFocusX, defaultFocusY;

    private static Point2D mouseOrigin;
    private static boolean canDrag = true;

    public static Camera getCamera() {
        return camera;
    }

    public static void setCanDrag(boolean value) {
        canDrag = value;
    }

    // Setup camera
    public static void defaultCamSetup() {
        Point2D envCenter = Game.world().environment().getCenter();
        MaxZoomOut = (float) ((float) 1 / ((envCenter.getX() + envCenter.getY()) * 0.004));
        zoomAmount = MaxZoomOut;
        defaultFocusX = 0;
        defaultFocusY = (float) envCenter.getY();
        camera.setFocus(defaultFocusX, defaultFocusY);
        camera.setZoom(zoomAmount, 0);
        Game.world().setCamera(camera);
    }

    public static void camFunction() {
        // Zoom-in / Zoom-out by MouseWheel
        Input.mouse().onWheelMoved(e -> {
            if (Input.keyboard().isPressed(17)) {
                if (e.getPreciseWheelRotation() == -1) {
                    zoomAmount += e.getScrollAmount() * 0.0125;
                } else {
                    zoomAmount -= e.getScrollAmount() * 0.0125;
                    if (zoomAmount <= MaxZoomOut) {
                        zoomAmount = MaxZoomOut;
                        camera.setFocus(defaultFocusX, defaultFocusY);
                    }
                }
                camera.setZoom(zoomAmount, 0);
            }
        });
        // Pan Camera by Dragging
        Input.mouse().onPressed(e -> {
            if (Input.keyboard().isPressed(17))
                mouseOrigin = Input.mouse().getMapLocation();
        });
        Input.mouse().onDragged(e -> {
            if (Input.keyboard().isPressed(17) && Input.mouse().isLeftButtonPressed() && canDrag) {
                Point2D pos = camera.getFocus();
                Point2D mouseLoc = Input.mouse().getMapLocation();
                double speed = 0.075;
                double diffX = (mouseOrigin.getX() - mouseLoc.getX()) * speed;
                double diffY = (mouseOrigin.getY() - mouseLoc.getY()) * speed;

                Point2D newPos = new Point2D.Double(pos.getX() + diffX, pos.getY() + diffY);

                camera.setFocus(newPos);
            }
        });
        // Pan Camera by WASD
        Input.keyboard().onKeyPressed(e -> {
            char keychar = e.getKeyChar();
            if (keychar == 'w' || keychar == 'a' || keychar == 's' || keychar == 'd') {
            int speed = 10;
                Point2D pos = camera.getFocus();
                Point2D newPos = null;
                if (keychar == 'w')
                    newPos = new Point2D.Double(pos.getX(), pos.getY() - speed);
                if (keychar == 'd')
                    newPos = new Point2D.Double(pos.getX() + speed, pos.getY());
                if (keychar == 'a')
                    newPos = new Point2D.Double(pos.getX() - speed, pos.getY());
                if (keychar == 's')
                    newPos = new Point2D.Double(pos.getX(), pos.getY() + speed);
                camera.setFocus(newPos);
            }
        });

        camera.onFocus(e -> InputController.entityOnCursor());
    }

    public static void dieScreenShake() {
        camera.shake(2, 0, 300);
    }
}
