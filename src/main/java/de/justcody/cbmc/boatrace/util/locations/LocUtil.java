package de.justcody.cbmc.boatrace.util.locations;

import de.justcody.cbmc.boatrace.game.map.LocationSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}