package de.einfachcody.boatrace.util;

import de.einfachcody.boatrace.game.arena.GoalLine;
import org.bukkit.Location;

public class LocationUtil {
    public static boolean isInBetween(Location loc, GoalLine goalLine) {
        Location l1 = LocationConverter.toBukkit(goalLine.getLoc1());
        Location l2 = LocationConverter.toBukkit(goalLine.getLoc2());
        return false;
    }
}
