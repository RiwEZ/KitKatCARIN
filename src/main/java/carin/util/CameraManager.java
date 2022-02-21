package carin.util;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.input.Input;


public class CameraManager {
    private static final Camera camera = new Camera();
    private static float zoomAmount;
    private static float MaxZoomOut;
    private static float defaultFocusX, defaultFocusY;
    private static float currentFocusX, currentFocusY;
    private static float mouseX, mouseY;

    public static Camera getCamera() {
        return camera;
    }

    // Setup camera
    public static void defaultCamSetup() {
        MaxZoomOut = (float) ((float) 1 / ((Game.world().environment().getCenter().getX() + Game.world().environment().getCenter().getY()) * 0.005));
        zoomAmount = MaxZoomOut;
        defaultFocusX = (float) (Game.world().environment().getCenter().getX() - Game.world().environment().getCenter().getX());
        defaultFocusY = (float) Game.world().environment().getCenter().getY();
        currentFocusX = defaultFocusX;
        currentFocusY = defaultFocusY;
        camera.setFocus(currentFocusX, currentFocusY);
        camera.setZoom(zoomAmount, 0);
        Game.world().setCamera(camera);
    }

    // still buggy...
    public static void camFunction() {
        // Zoom-in / Zoom-out by MouseWheel
        Input.mouse().onWheelMoved(x -> {
            if(Input.keyboard().isPressed(17)){
                if(x.getPreciseWheelRotation() == -1){
                    zoomAmount += x.getScrollAmount() * 0.0125;
                }
                else{
                    zoomAmount -= x.getScrollAmount() * 0.0125;
                    if(zoomAmount <= MaxZoomOut){
                        zoomAmount = MaxZoomOut;
                        camera.setFocus(defaultFocusX, defaultFocusY);
                    }
                }
                camera.setZoom(zoomAmount, 0);
            }
        });
        // Pan Camera by Dragging
        Input.mouse().onPressed(k -> {
            mouseX = k.getXOnScreen();
            mouseY = k.getYOnScreen();
        });
        Input.mouse().onDragged(x -> {
            if(Input.keyboard().isPressed(17)){
                if (zoomAmount > MaxZoomOut + 0.1) {
                    if(Math.abs((x.getXOnScreen() - mouseX)) > Math.abs((x.getYOnScreen() - mouseY))){
                        if(x.getXOnScreen() > mouseX){
                            currentFocusX -= 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                        if (x.getXOnScreen() < mouseX){
                            currentFocusX += 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                    }
                    if (Math.abs((x.getYOnScreen() - mouseY)) > Math.abs((x.getXOnScreen() - mouseX))){
                        if(x.getYOnScreen() > mouseY){
                            currentFocusY -= 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                        if (x.getYOnScreen() < mouseY){
                            currentFocusY += 1.5;
                            camera.setFocus(currentFocusX, currentFocusY);
                        }
                    }
                }
            }
        });
    }

    public static void dieScreenShake() {
        camera.shake(2,0, 300);
    }
}
