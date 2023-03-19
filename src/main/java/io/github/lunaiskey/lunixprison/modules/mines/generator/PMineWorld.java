package io.github.lunaiskey.lunixprison.modules.mines.generator;

import org.bukkit.*;

public class PMineWorld {

    private static final String WORLD_NAME = "lunixmines";

    public void generateWorld() {
        WorldCreator wc = new WorldCreator(WORLD_NAME);
        wc.biomeProvider(new MineBiomeProvider());
        wc.generator(new MineWorldGenerator());
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.environment(World.Environment.NORMAL);
        World world = wc.createWorld();
        if (world == null) return;
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setSpawnFlags(false,false);
        world.setGameRule(GameRule.DO_TRADER_SPAWNING,false);
    }

    public static String getWorldName() {
        return WORLD_NAME;
    }
}
