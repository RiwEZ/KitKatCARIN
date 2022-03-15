package carin.util;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.sound.Sound;

import java.util.Collection;

public class SoundManager {
    private static final Sound knockSound = Resources.sounds().get("audio/knock_b.wav");
    private static final Sound lastHit = Resources.sounds().get("audio/lasthit_b.wav");
    private static final Sound atk1 = Resources.sounds().get("audio/atk3_b.wav");
    private static final Sound atk2 = Resources.sounds().get("audio/atk4_b.wav");
    private static final Sound move = Resources.sounds().get("audio/move.wav");
    private static final Sound spawn = Resources.sounds().get("audio/spawn.wav");
    private static final Sound tick = Resources.sounds().get("audio/tick.wav");
    private static final Sound purchase = Resources.sounds().get("audio/purchase.ogg");
    private static final Sound wrong = Resources.sounds().get("audio/wrong.ogg");
    private static final Sound menu = Resources.sounds().get("audio/bgm/menu.mp3");
    private static final Sound ingame = Resources.sounds().get("audio/bgm/ingame.mp3");

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
    public static void tickSound() {
        Game.audio().playSound(tick,false,10,0.15f);
    }

    public static void purchaseSound() {
        Game.audio().playSound(purchase,false,10,0.35f);
    }

    public static void deniedSound() {
        Game.audio().playSound(wrong,false,10,0.35f);
    }
    public static void menu() { Game.audio().playMusic(menu); }
    public static void ingame() {Game.audio().playMusic(ingame);}
}
