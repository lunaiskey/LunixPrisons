package io.github.lunaiskey.lunixprison.mines.generator;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class PMineWorld {

    private final static String worldName = "mines";

    public void generateWorld() {
        WorldCreator wc = new WorldCreator(worldName);
        wc.biomeProvider(new MineBiomeProvider());
        wc.generator(new MineWorldGenerator());
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.environment(World.Environment.NORMAL);
        wc.createWorld();
    }

    public static String getWorldName() {
        return worldName;
    }

}
