package carin;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private static Properties config;

    static {
        try {
            // Path.of(config file location)
            Reader reader = Files.newBufferedReader(Path.of("gameconfig.properties"));
            config = new Properties();
            config.load(reader);
        }
        catch (IOException e) {e.printStackTrace();}
    }

    private static int get(String key) {
        return Integer.parseInt(config.getProperty(key));
    }

    public static final int tile_width = 36;
    public static final int tile_height = 36;

    public static final int map_m = get("map.m");
    public static final int map_n = get("map.n");
    public static final float spawn_rate = Float.parseFloat(config.getProperty("spawn_rate"));

    public static final int initial_credits = get("initial_credits");

    public static final int placement_cost = get("placement_cost");
    public static final int move_hp_cost = get("move_hp_cost");
    public static final int antibody_cred_gain = get("antibody_cred_gain");

    public static final int antibody_hp = get("antibody_hp");
    public static final int antibody_dmg = get("antibody_dmg");
    public static final int antibody_leech = get("antibody_leech");

    public static final int virus_hp = get("virus_hp");
    public static final int virus_dmg = get("virus_dmg");
    public static final int virus_leech = get("virus_leech");
}
