package io.github.lunaiskey.lunixprison.util.nms;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightInterface;
import ca.spottedleaf.starlight.common.util.WorldUtil;
import it.unimi.dsi.fastutil.shorts.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import java.util.*;

public class NMSBlockChange {

    private final org.bukkit.World bukkitWorld;
    private final Level world;
    private final HashMap<BlockPos, BlockState> modified = new HashMap<>();

    public NMSBlockChange(org.bukkit.World bukkitWorld, Level world) {
        this.bukkitWorld = bukkitWorld;
        this.world = world;
    }

    public NMSBlockChange(World bukkitWorld) {
        this(bukkitWorld,((CraftWorld) bukkitWorld).getHandle());

    }

    public void setBlock(int x, int y, int z, org.bukkit.Material material) {
        BlockState blockState = CraftMagicNumbers.getBlock(material).defaultBlockState();
        BlockPos blockPos = new BlockPos(x, y, z);
        modified.put(blockPos, blockState);
    }

    public Material getBlock(int x, int y, int z) {
        BlockState data = modified.get(new BlockPos(x, y, z));
        if (data != null)
            return CraftMagicNumbers.getMaterial(data).getItemType();
        return new Location(bukkitWorld, x, y, z).getBlock().getType();
    }

    public void update() {
        //modify blocks
        ThreadedLevelLightEngine engine = (ThreadedLevelLightEngine) world.getChunkSource().getLightEngine();
        //Set<LevelChunk> chunks = new HashSet<>();
        //Set<SectionPos> sectionPosSet = new HashSet<>();
        Map<ChunkPos, Map<SectionPos, Short2ObjectMap<BlockState>>> map = new HashMap<>();
        for (Map.Entry<BlockPos, BlockState> entry : modified.entrySet()) {
            BlockPos pos = entry.getKey();
            LevelChunk chunk = world.getChunkSource().getChunk(pos.getX() >> 4, pos.getZ() >> 4, true);
            Map<SectionPos, Short2ObjectMap<BlockState>> i = map.get(chunk.getPos());
            if (i == null) {
                i = new HashMap<>();
                map.put(chunk.getPos(), i);
            }
            SectionPos sectionPos = SectionPos.of(pos);
            Short2ObjectMap<BlockState> i1 = i.get(sectionPos);
            if (i1 == null) {
                i1 = new Short2ObjectArrayMap<>();
                i.put(sectionPos, i1);
            }
            i1.put(SectionPos.sectionRelativePos(pos), entry.getValue());

            chunk.setBlockState(pos, entry.getValue(), false);
            //Modify Light nibbles
            SWMRNibbleArray[] skyNibbles = chunk.getSkyNibbles();
            int sectionY = (pos.getY() >> 4) - WorldUtil.getMinLightSection(world);
            SWMRNibbleArray skyNibble = skyNibbles[sectionY];
            skyNibble.set(pos.getX(), pos.getY(), pos.getZ(), 15);
            skyNibbles[sectionY] = skyNibble;
            chunk.setSkyNibbles(skyNibbles);
            //sectionPosSet.add(sectionPos);
            //LunixPrison.getPlugin().getLogger().info(pos.getX()+" "+pos.getY()+" "+pos.getZ()+": "+starLightInterface.getSkyReader().getLightValue(pos));
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getLocation().getWorld().getName().equalsIgnoreCase(bukkitWorld.getName())) {
                continue;
            }
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            int dist = Bukkit.getViewDistance() + 1;
            //ClientboundForgetLevelChunkPacket unload = new ClientboundForgetLevelChunkPacket(chunk.getPos().x, chunk.getPos().z);
            //ClientboundLevelChunkWithLightPacket load = new ClientboundLevelChunkWithLightPacket(chunk, engine, null, null, true,false);

            //ClientboundBlockUpdatePacket
            //ClientboundLightUpdatePacket light = new ClientboundLightUpdatePacket(chunk.getPos(),engine,null,null, true);
            for (Map.Entry<ChunkPos, Map<SectionPos, Short2ObjectMap<BlockState>>> entry : map.entrySet()) {
                ChunkPos chunkPos = entry.getKey();
                int chunkX = ep.chunkPosition().x;
                int chunkZ = ep.chunkPosition().z;
                if (chunkPos.x < chunkX - dist || chunkPos.x > chunkX + dist || chunkPos.z < chunkZ - dist || chunkPos.z > chunkZ + dist) {
                    continue;
                }
                Map<SectionPos, Short2ObjectMap<BlockState>> i1 = entry.getValue();
                ClientboundLightUpdatePacket light = new ClientboundLightUpdatePacket(chunkPos, engine, null, null, true);
                ep.connection.send(light);
                for (Map.Entry<SectionPos, Short2ObjectMap<BlockState>> entry1 : i1.entrySet()) {
                    ClientboundSectionBlocksUpdatePacket blockupdate = new ClientboundSectionBlocksUpdatePacket(entry1.getKey(), entry1.getValue(), true);
                    ep.connection.send(blockupdate);
                }
                /*
                if (i1.size() == 1) {
                    Map.Entry<SectionPos, Short2ObjectMap<BlockState>> entry1 = i1.entrySet().iterator().next();
                    Short2ObjectMap.Entry<BlockState> e = entry1.getValue().short2ObjectEntrySet().iterator().next();
                    BlockPos blockPos = entry1.getKey().relativeToBlockPos(e.getShortKey());
                    BlockState blockState = e.getValue();
                    ClientboundBlockUpdatePacket blockupdate = new ClientboundBlockUpdatePacket(blockPos,blockState);
                    ep.connection.send(blockupdate);
                } else {

                }

                 */
                //ep.connection.send(unload);
                //ep.connection.send(load);

            }
        }
        //clear modified blocks
        modified.clear();
    }
}
