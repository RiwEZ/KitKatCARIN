package carin.gui;

import carin.Config;
import carin.GameStates;
import carin.entities.Antibody;
import carin.entities.Player;
import carin.util.CameraManager;
import carin.util.InputController;
import carin.util.SoundManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.DropdownListField;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AntibodyShop extends ImageComponent {
    private final DropdownListField ddList;
    private boolean isBuyPress;
    private int current;

    private List<BufferedImage> antibody;
    private List<BufferedImage> cursor;

    public AntibodyShop() {
        super(164, 175, 72, 72);
        initBufferedImage();

        Object[] arr = antibodies().toArray();
        ddList = new DropdownListField(50, 450, 300, 100, arr, arr.length);
        current = 0;

        ddList.setSelection(current);
        ddList.onChange(this::changeImage);

        this.setImage(antibody.get(current));
        initEventListener();
    }

    public DropdownListField getDdList() {
        return ddList;
    }

    private List<Antibody> antibodies() {
        return GameStates.states().getAvailableAntibody();
    }

    private void initBufferedImage() {
        antibody = new ArrayList<>();
        cursor = new ArrayList<>();

        antibodies().forEach(ab -> {
            String sprite = ab.getSpritesheetName();
            antibody.add(Resources.images().get("sprites/" + sprite + "-shop.png"));
            cursor.add(Resources.images().get("misc/" + sprite + "-cursor.png"));
        });
    }

    private void initEventListener() {
        BufferedImage Cursor = Resources.images().get("misc/cursor.png");
        isBuyPress = false;

        this.onMousePressed(k -> {
            if (Input.keyboard().isPressed(17)) return;
            isBuyPress = true;
            Game.window().cursor().set(cursor.get(current));
            GameStates.loop().setPause(true);
            InputController.setSpacebarToggle(false);
            CameraManager.setCanDrag(false);
        });

        Input.mouse().onReleased(e -> {
            if (Input.mouse().isLeftButton(e)) buyingEvent();

            Game.window().cursor().set(Cursor);
            InputController.setSpacebarToggle(true);
            CameraManager.setCanDrag(true);
            isBuyPress = false;
        });
    }

    private void buyingEvent() {
        GameStates states = GameStates.states();
        Player player = Player.instance();
        Point2D mouseManual = states.getSnap(Input.mouse().getMapLocation());

        if (isBuyPress && states.isInMap(mouseManual) && states.isUnOccupied(mouseManual)) {
            int cost = Config.placement_cost;

            if (player.getCredit() - cost >= 0) {
                player.addCredit(-cost);
                SoundManager.purchaseSound();
                if (ddList.getSelectedIndex() == -1)
                    states.spawnGeneticEntity(mouseManual, selectedAntibody(0));
                else
                    states.spawnGeneticEntity(mouseManual, selectedAntibody(current));

            } else SoundManager.deniedSound();
        }
    }

    private Antibody selectedAntibody(int index) {
        return (Antibody) antibodies().get(index).getCopy();
    }

    private void changeImage(int index) {
        this.setImage(antibody.get(index));
        current = index;
    }

    public void changeZero() {
        changeImage(0);
    }

}
