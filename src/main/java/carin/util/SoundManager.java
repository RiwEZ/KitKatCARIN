package carin.util;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.sound.Sound;

import java.util.Collection;

public class SoundManager {
    private static final Sound knockSound = Resources.sounds().get("audio/knock_b.wav");
    private static final Sound lastHit = Resources.sounds().get("audio/lasthit_b.wav");
    private static final Sound atk1 = Resources.sounds().get("audio/atk1_b.wav");
    private static final Sound atk2 = Resources.sounds().get("audio/atk2_b.wav");
    private static final Sound move = Resources.sounds().get("audio/move.wav");
    private static final Sound spawn = Resources.sounds().get("audio/spawn.wav");

    public static void knockSound() {
        Game.audio().playSound(knockSound,false,50,0.15f);
    }

    public static void lastHitSound() {
        Game.audio().playSound(lastHit,false,10,0.15f);
    }

    public static void attackSound() {
        if(Math.random() < 0.5) Game.audio().playSound(atk1,false,10,0.025f);
        else Game.audio().playSound(atk2,false,10,0.025f);
    }

    public static void moveSound() {
        Game.audio().playSound(move,false,10,0.005f);
    }

    public static void spawnSound() {
        Game.audio().playSound(spawn,false,10,0.15f);
    }
}
