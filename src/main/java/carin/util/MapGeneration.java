package carin.util;

import carin.Config;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.tilemap.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.resources.Maps;
import de.gurkenlabs.litiengine.resources.Resources;

public final class MapGeneration {

    public static IMap map;
    public static IMap generateMap(int m, int n) {
        final String MAP_NAME = "Spawn_MAP";
        final int TILE_WIDTH = Config.tile_width;
        final int TILE_HEIGHT = Config.tile_height;

        try (Maps.MapGenerator generator = Resources.maps().generate(MapOrientations.ORTHOGONAL, MAP_NAME, m, n, TILE_WIDTH, TILE_HEIGHT, Resources.tilesets().get("map/tiles-club.tsx"))) {
            // add spawn points
            generator.addTileLayer(RenderType.NONE, (x,y) -> {
                Spawnpoint s = new Spawnpoint(x * TILE_WIDTH,y * TILE_HEIGHT);
                s.setSize(TILE_WIDTH, TILE_HEIGHT);
                generator.add(s);
                return -1; // return tile texture id -1 is no texture
            });
            map = generator.getMap();
        }
        return map;
    }

}
