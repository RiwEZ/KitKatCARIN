package carin;

import carin.entities.Antibody;
import carin.entities.Player;
import carin.entities.Virus;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;

import java.util.Random;


public final class GameStates {
    private static final Random rand = new Random();;
    private GameStates() {
    }
    public static void init() {
        Camera camera = new FreeFlightCamera();
        camera.setClampToMap(true);
        Game.world().setCamera(camera);
        Game.world().onLoaded(e -> {
            Player p = Player.instance();
            // test spawn
            for(int i = 0; i < 3; i++){
                int num = (rand.nextInt(54) + 6) % 54;
                Spawnpoint enter = e.getSpawnpoint(num);
                if(enter != null)
                enter.spawn(new Virus());
            }
            // test spawn
            for(int i = 0; i < 3; i++){
                int num = (rand.nextInt(54) + 6) % 54;
                Spawnpoint enter = e.getSpawnpoint(num);
                if(enter != null){
                    Antibody a = new Antibody();
                    p.addAntibody(a);
                    enter.spawn(a);
                }

            }
        });
    }
}
