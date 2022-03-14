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

    public static Camera getCamera() {
        return camera;
    }

    // Setup camera
    public static void defaultCamSetup() {
        MaxZoomOut = (float) ((float) 1 / ((Game.world().environment().getCenter().getX() + Game.world().environment().getCenter().getY()) * 0.004));
        zoomAmount = MaxZoomOut;
        defaultFocusX = 0;
        defaultFocusY = (float) Game.world().environment().getCenter().getY();
        camera.setFocus(defaultFocusX, defaultFocusY);
        camera.setZoom(zoomAmount, 0);
        Game.world().setCamera(camera);
    }

    // still buggy...
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
        Input.mouse().onPressed(e -> mouseOrigin = Input.mouse().getMapLocation());
        Input.mouse().onDragged(e -> {
            if (Input.keyboard().isPressed(17)) {
                if (zoomAmount > MaxZoomOut + 0.1) {
                    Point2D pos = camera.getFocus();
                    Point2D mouseLoc = Input.mouse().getMapLocation();
                    double speed = 0.075;
                    double diffX = (mouseOrigin.getX() - mouseLoc.getX()) * speed;
                    double diffY = (mouseOrigin.getY() - mouseLoc.getY()) * speed;

                    Point2D newPos = new Point2D.Double(pos.getX() + diffX, pos.getY() + diffY);

                    camera.setFocus(newPos);
                }
            }
        });

        Input.keyboard().onKeyPressed(e -> {
            int speed = 10;
            if (zoomAmount > MaxZoomOut + 0.1) {
                Point2D pos = camera.getFocus();
                Point2D newPos = null;
                if (e.getKeyChar() == 'w') {
                    newPos = new Point2D.Double(pos.getX(), pos.getY() - speed);
                }
                if (e.getKeyChar() == 'd') {
                    newPos = new Point2D.Double(pos.getX() + speed, pos.getY());
                }
                if (e.getKeyChar() == 'a') {
                    newPos = new Point2D.Double(pos.getX() - speed, pos.getY());
                }
                if (e.getKeyChar() == 's') {
                    newPos = new Point2D.Double(pos.getX(), pos.getY() + speed);
                }
                camera.setFocus(newPos == null ? pos : newPos);
            }
        });

    }

    public static void dieScreenShake() {
        camera.shake(2, 0, 300);
    }
}
