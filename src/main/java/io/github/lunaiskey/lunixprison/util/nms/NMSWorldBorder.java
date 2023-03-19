package io.github.lunaiskey.lunixprison.util.nms;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSWorldBorder {

    public void sendBorder(Player player, int size, int centerX, int centerZ) {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.setCenter(centerX,centerZ);
        worldBorder.setSize(size);
        worldBorder.setWarningBlocks(0);
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        worldBorder.world = ((CraftPlayer) player).getHandle().getLevel();
        connection.send(new ClientboundInitializeBorderPacket(worldBorder));
        sendPackets(connection,worldBorder);
    }

    private void sendPackets(ServerGamePacketListenerImpl connection, WorldBorder worldBorder) {
        connection.send(new ClientboundSetBorderSizePacket(worldBorder));
        connection.send(new ClientboundSetBorderCenterPacket(worldBorder));
        connection.send(new ClientboundSetBorderWarningDistancePacket(worldBorder));
    }
}
