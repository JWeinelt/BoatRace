package de.justcody.cbmc.boatrace.util.locations;

import de.justcody.cbmc.boatrace.game.map.LocationSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocUtil {
    @NotNull
    public static Location fromWrapper(LocationWrapper w) {
        return new Location(Bukkit.getWorld(w.getWorld()),
                w.getX(),w.getY(),w.getZ(),w.getYaw(),w.getPitch());
    }


    @Nullable
    public static LocationWrapper fromBukkit(Location l) {
        if (l.getWorld() == null) return null;
        return new LocationWrapper(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }


    public static boolean isInArea(Location playerLocation, Location loc1, Location loc2) {
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return playerLocation.getX() >= minX && playerLocation.getX() <= maxX &&
                playerLocation.getY() >= minY && playerLocation.getY() <= maxY &&
                playerLocation.getZ() >= minZ && playerLocation.getZ() <= maxZ;
    }


    public static boolean isInArea(Location playerLocation, LocationSection section) {
        Location loc1 = fromWrapper(section.getL1());
        Location loc2 = fromWrapper(section.getL2());
        return isInArea(playerLocation, loc1, loc2);
    }


    public static LocationWrapper getMiddle(LocationSection section) {
        String world = section.getL1().getWorld();  // Assuming both locations are in the same world
        double middleX = (section.getL1().getX() + section.getL2().getX()) / 2;
        double middleY = (section.getL1().getY() + section.getL2().getY()) / 2;
        double middleZ = (section.getL1().getZ() + section.getL2().getZ()) / 2;
        float middleYaw = (section.getL1().getYaw() + section.getL2().getYaw()) / 2;
        float middlePitch = (section.getL1().getPitch() + section.getL2().getPitch()) / 2;

        return new LocationWrapper(world, middleX, middleY, middleZ, middleYaw, middlePitch);
    }

    public static List<Material> getBlocksAround(Location location) {
        List<Material> blocks = new ArrayList<>();
        Location l = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

        blocks.add(location.add(0, 0, 1).getBlock().getType());
        location = l;
        blocks.add(location.add(1, 0, 1).getBlock().getType());
        location = l;
        blocks.add(location.add(-1, 0, 1).getBlock().getType());
        location = l;
        blocks.add(location.add(0, 0, -1).getBlock().getType());
        location = l;
        blocks.add(location.add(1, 0, -1).getBlock().getType());
        location = l;
        blocks.add(location.add(-1, 0, -1).getBlock().getType());
        location = l;
        blocks.add(location.add(1, 0, 0).getBlock().getType());
        location = l;
        blocks.add(location.add(-1, 0, 0).getBlock().getType());

        return blocks;
    }
}