package carin.util;

import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.tilemap.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.resources.Maps;
import de.gurkenlabs.litiengine.resources.Resources;

public final class MapGeneration {

    public static IMap map;
    public static IMap generateMap(int m, int n) {

        final String MAP_NAME = "Spawn_MAP";
        final int TILE_WIDTH = 36;
        final int TILE_HEIGHT = 36;

        ITileLayer tileLayer;

        try (Maps.MapGenerator generator = Resources.maps().generate(MapOrientations.ORTHOGONAL, MAP_NAME, m, n, TILE_WIDTH, TILE_HEIGHT, Resources.tilesets().get("map/tiles-club.tsx"))) {
            // add spawn points
            tileLayer = generator.addTileLayer(RenderType.NONE, (x,y) -> {
                Spawnpoint s = new Spawnpoint(x * 36,y * 36);
                s.setWidth(36);
                s.setHeight(36);
                generator.add(s);
                return -1; // return tile texture id -1 is no texture
            });
            map = generator.getMap();
        }
        return map;
    }

}
