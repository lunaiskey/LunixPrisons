package io.github.lunaiskey.lunixprison.modules.mines.generator;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class PMineWorld {

    private final static String WORLD_NAME = "mines";

    public void generateWorld() {
        WorldCreator wc = new WorldCreator(WORLD_NAME);
        wc.biomeProvider(new MineBiomeProvider());
        wc.generator(new MineWorldGenerator());
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.environment(World.Environment.NORMAL);
        wc.createWorld();
    }

    public static String getWorldName() {
        return WORLD_NAME;
    }

}
