package carin.util;

import carin.Config;
import carin.GameStates;
import de.gurkenlabs.litiengine.environment.tilemap.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.resources.Maps;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.geom.Point2D;

public final class MapGeneration {

    public static IMap map;
    public static IMap generateMap(int m, int n) {
        final String MAP_NAME = "Spawn_MAP";
        final int TILE_WIDTH = Config.tile_width;
        final int TILE_HEIGHT = Config.tile_height;

        try (Maps.MapGenerator generator =
                     Resources.maps()
                             .generate(MapOrientations.ORTHOGONAL, MAP_NAME, m, n,
                                     TILE_WIDTH, TILE_HEIGHT, Resources.tilesets().get("map/tiles-club.tsx"))) {


            GameStates states = GameStates.states();
            generator.addTileLayer(RenderType.BACKGROUND, (x, y) -> {
                Point2D s = new Point2D.Double(x * TILE_WIDTH,y * TILE_HEIGHT);
                states.entityMap().put(s, states.unOccupied());
                return -1;
            });
            map = generator.getMap();
        }
        return map;
    }

}
