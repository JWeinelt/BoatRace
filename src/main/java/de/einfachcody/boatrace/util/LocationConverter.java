package de.einfachcody.boatrace.util;

import de.einfachcody.boatrace.game.arena.LocationWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationConverter {
    public static LocationWrapper fromBukkit(Location location) {
        return new LocationWrapper(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public static Location toBukkit(LocationWrapper wrapper) {
        return new Location(
                Bukkit.getWorld(wrapper.getWorld()),
                wrapper.getX(),
                wrapper.getY(),
                wrapper.getZ(),
                wrapper.getYaw(),
                wrapper.getPitch()
        );
    }

    public static List<Location> toBukkitList(List<LocationWrapper> wrapperList) {
        List<Location> res = new ArrayList<>();
        for (LocationWrapper w : wrapperList) res.add(toBukkit(w));
        return res;
    }

}
