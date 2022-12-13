package io.github.lunaiskey.lunixprison.util;


import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ShapeUtil {

    public static List<Location> generateSphere(Location center,int radius) {
        List<Location> circleBlocks = new ArrayList<>();
        int bX = center.getBlockX();
        int bY = center.getBlockY();
        int bZ = center.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x)*(bX - x)) + ((bY - y)*(bY - y)) + ((bZ - z)*(bZ - z));
                    if (distance < radius * radius) {
                        Location l = new Location(center.getWorld(),x,y,z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }
}
