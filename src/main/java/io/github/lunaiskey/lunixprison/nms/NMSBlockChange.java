package io.github.lunaiskey.lunixprison.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NMSBlockChange {

    private final org.bukkit.World bukkitWorld;
    private final Level world;
    private final HashMap<BlockPos, BlockState> modified = new HashMap<>();

    public NMSBlockChange(org.bukkit.World bukkitWorld, Level world) {
        this.bukkitWorld = bukkitWorld;
        this.world = world;
    }

    public void setBlock(int x, int y, int z, org.bukkit.Material material) {
        modified.put(new BlockPos(x, y, z), CraftMagicNumbers.getBlock(material).defaultBlockState());
    }

    public Material getBlock(int x, int y, int z) {
        BlockState data = modified.get(new BlockPos(x, y, z));
        if (data != null)
            return CraftMagicNumbers.getMaterial(data).getItemType();
        return new Location(bukkitWorld, x, y, z).getBlock().getType();
    }

    public void update() {
        //modify blocks
        HashSet<LevelChunk> chunks = new HashSet<>();
        for (Map.Entry<BlockPos, BlockState> entry : modified.entrySet()) {
            LevelChunk chunk = world.getChunkSource().getChunk(entry.getKey().getX() >> 4, entry.getKey().getZ() >> 4, true);
            chunks.add(chunk);
            chunk.setBlockState(entry.getKey(), entry.getValue(), false);
        }


        LevelLightEngine engine = world.getChunkSource().getLightEngine();
        for (BlockPos pos : modified.keySet()) {
            engine.checkBlock(pos);
        }


        //unload & load chunk data
        for (LevelChunk chunk : chunks) {
             ClientboundForgetLevelChunkPacket unload = new ClientboundForgetLevelChunkPacket(chunk.getPos().x, chunk.getPos().z);

            ClientboundLevelChunkWithLightPacket load = new ClientboundLevelChunkWithLightPacket(chunk,engine,null,null, true);
            //ClientboundLightUpdatePacket light = new ClientboundLightUpdatePacket(chunk.getPos(),engine,null,null, true);


            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().getWorld().getName().equalsIgnoreCase(bukkitWorld.getName())) {
                    ServerPlayer ep = ((CraftPlayer) p).getHandle();
                    int dist = Bukkit.getViewDistance() + 1;
                    int chunkX = ep.chunkPosition().x;
                    int chunkZ = ep.chunkPosition().z;
                    if (chunk.getPos().x < chunkX - dist ||
                            chunk.getPos().x > chunkX + dist ||
                            chunk.getPos().z < chunkZ - dist ||
                            chunk.getPos().z > chunkZ + dist) continue;
                    ep.connection.send(unload);
                    ep.connection.send(load);
                    //ep.connection.send(light);
                }
            }
        }

        //clear modified blocks
        modified.clear();
    }
}
